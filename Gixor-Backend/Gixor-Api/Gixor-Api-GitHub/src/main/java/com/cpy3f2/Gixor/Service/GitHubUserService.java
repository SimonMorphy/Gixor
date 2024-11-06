package com.cpy3f2.Gixor.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Convertor.UserConvertor;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.Query.UserQueryBuilder;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.SimpleUser;
import com.cpy3f2.Gixor.Domain.StatisticUser;
import com.cpy3f2.Gixor.Exception.constant.GitHubErrorCodes;
import com.cpy3f2.Gixor.Exception.user.UserOperationException;
import com.cpy3f2.Gixor.Exception.util.GitHubErrorMessageUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;


/**
 * @author : simon
 * @description :
 * @last : 2024-11-01 00:31
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-01 00:31
 */
@Slf4j
@Service
public class GitHubUserService {

    @Resource
    private WebClient githubClient;

    @Resource
    @Qualifier("gitHubClientWithoutToken")
    private WebClient gitHubClientWithoutToken;

    @Resource
    private ReactiveElasticsearchOperations esOperations;

    @Resource
    private GitHubErrorMessageUtil errorMessageUtil;




    public Mono<GitHubUser> getInfo(){
        return githubClient
                .get()
                .uri("/user")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        )))
                .bodyToMono(GitHubUser.class)
                .flatMap(this::getStats);
    }

    public Mono<GitHubUser> getGitHubUser(String username) {
        return gitHubClientWithoutToken
                .get()
                .uri("/users/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        )))
                .bodyToMono(GitHubUser.class);
    }
    public Mono<GitHubUser> getUserDetail(String username) {
        return githubClient.post()
                .uri("/graphql")
                .bodyValue(UserQueryBuilder.buildUserQuery(username))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> Mono.fromCallable(() -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response);
                    
                    // 检查错
                    if (rootNode.has(Constants.ERROR)) {
                        throw new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        );
                    }
                    
                    // 获取用户节点
                    JsonNode userNode = rootNode.path("data").path("user");
                    if (userNode.isMissingNode() || userNode.isNull()) {
                        throw new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_NOT_FOUND),
                                new ResponseResult()
                        );
                    }
                    return userNode;
                }))
                .map(UserConvertor::convertToGitHubUser)
                .flatMap(this::getStats)
                .onErrorMap(e -> {
                    if (e instanceof UserOperationException) {
                        return e;
                    }
                    log.error("获取用户详情失败: {}", e.getMessage(), e);
                    return new UserOperationException(
                            errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                            new ResponseResult()
                    );
                });
    }


    /**
     * 根据用户名获取用户信息并补充统计数据
     */
    public Mono<GitHubUser> getInfo(String username) {
        return getGitHubUser(username)
                .flatMap(this::getStats);
    }
    /**
     * 尝试从ES获取统计数据并补充，如果没有则返回原始数据
     */
    private Mono<GitHubUser> getStats(GitHubUser githubUser) {
        if (githubUser == null || githubUser.getGithubId() == null) {
            return Mono.just(githubUser);
        }
        
        return esOperations.search(
                NativeQuery.builder()
                        .withQuery(q -> q
                                .bool(b -> b
                                        .should(s -> s
                                                .term(t -> t
                                                        .field("id")
                                                        .value(extractNumericId(githubUser.getGithubId()))
                                                ))
                                        .should(s -> s
                                                .term(t -> t
                                                        .field("login.keyword")
                                                        .value(githubUser.getLogin())
                                                ))
                                )
                        )
                        .build()
                , GitHubUser.class)
                .map(SearchHit::getContent)
                .next()
                .map(statsUser -> {
                    // 保存原始ID
                    String originalId = githubUser.getGithubId();
                    
                    // 手动映射ES字段到实体类
                    githubUser.setTotalStars(statsUser.getTotalStars());
                    githubUser.setTotalCommits(statsUser.getTotalCommits());
                    githubUser.setTotalPRs(statsUser.getTotalPRs());
                    githubUser.setTotalPRsMerged(statsUser.getTotalPRsMerged());
                    githubUser.setMergedPRsPercentage(statsUser.getMergedPRsPercentage());
                    githubUser.setTotalPRsReviewed(statsUser.getTotalPRsReviewed());
                    githubUser.setTotalIssues(statsUser.getTotalIssues());
                    githubUser.setTotalDiscussionsStarted(statsUser.getTotalDiscussionsStarted());
                    githubUser.setTotalDiscussionsAnswered(statsUser.getTotalDiscussionsAnswered());
                    githubUser.setContributedTo(statsUser.getContributedTo());
                    githubUser.setGrade(statsUser.getGrade());
                    githubUser.setScore(statsUser.getScore());
                    githubUser.setMajorDomains(statsUser.getMajorDomains());
                    githubUser.setDomainWeights(statsUser.getDomainWeights());
                    githubUser.setNation(statsUser.getNation());
                    
                    // 恢复原始ID
                    githubUser.setGithubId(originalId);
                    return githubUser;
                })
                .defaultIfEmpty(githubUser);
    }

    private String extractNumericId(String graphqlId) {
        if (graphqlId == null) {
            return null;
        }
        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(graphqlId));
            return decoded.replaceAll("\\D+", "");
        } catch (Exception e) {
            return graphqlId;
        }
    }

    /**
     * 获取GitHub用户排行榜
     * @param page 页码
     * @param size 每页大小
     * @return Flux<GitHubUser> 用户列表（按score降序）
     */
    public Flux<StatisticUser> getRanking(int page, int size) {
        return esOperations.search(
                NativeQuery.builder()
                        .withQuery(q -> q.matchAll(m -> m))
                        .withSort(sort -> sort
                                .field(f -> f
                                        .field("score")
                                        .order(SortOrder.Desc)
                                )
                        )
                        .withPageable(PageRequest.of(page, size))
                        .build(),
                StatisticUser.class
        ).map(SearchHit::getContent);
    }
    public Flux<SimpleUser> getFollowers(BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/user/followers"),
                        querySetting
                ).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        )))
                .bodyToFlux(SimpleUser.class);
    }
    public Flux<SimpleUser> getFollowing(BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/user/following"),
                        querySetting
                ).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        )))
                .bodyToFlux(SimpleUser.class);
    }
    public Mono<Void> checkFollowed(String username){
        return githubClient
                .get()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_NOT_FOLLOWED),
                                new ResponseResult()
                        )))
                .bodyToMono(Void.class);
    }
    public Mono<Void> follow(String username){
        return githubClient
                .put()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_FOLLOW),
                                new ResponseResult()
                        )))
                .bodyToMono(Void.class);
    }
    public Mono<Void> unfollow(String username){
        return githubClient
                .delete()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_UNFOLLOW),
                                new ResponseResult()
                        )))
                .bodyToMono(Void.class);
    }
    public Flux<SimpleUser> listFollowers(String username, BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/followers"),
                        querySetting
                ).build(username))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("搜索失败"))
                )
                .bodyToFlux(SimpleUser.class);
    }
    public Flux<SimpleUser> listFollowing(String username, BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/following"),
                        querySetting
                ).build(username))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("搜索失败"))
               ).bodyToFlux(SimpleUser.class);
    }

    public Flux<String> getAllNations() {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withFields(Collections.singletonList("nation"))
                .withSort(Sort.by(Sort.Direction.ASC, "nation.keyword"))
                .withPageable(PageRequest.of(0, 1000))
                .build();
                
        return esOperations.searchForPage(query, GitHubUser.class)
                .expand(searchHits -> {
                    if (searchHits.hasNext()) {
                        return esOperations.searchForPage(
                                NativeQuery.builder()
                                        .withQuery(q -> q.matchAll(m -> m))
                                        .withFields(Collections.singletonList("nation"))
                                        .withSort(Sort.by(Sort.Direction.ASC, "nation.keyword"))
                                        .withPageable(PageRequest.of(searchHits.getNumber() + 1, 1000))
                                        .build(),
                                GitHubUser.class
                        );
                    }
                    return Mono.empty();
                })
                .flatMap(page -> Flux.fromIterable(page.getContent()))
                .map(searchHit -> searchHit.getContent().getNation())
                .filter(nation -> nation != null && !nation.trim().isEmpty())
                .distinct();
    }

    /**
     * 获取指定国家/地区的GitHub用户排行榜
     * @param nation 国家/地区
     * @param page 页码
     * @param size 每页大小
     * @return Flux<GitHubUser> 用户列表（按score降序）
     */
    public Flux<StatisticUser> getRankingByNation(String nation, int page, int size) {
        return esOperations.search(NativeQuery.builder()
                                .withQuery(q -> q
                                        .bool(b -> b
                                                .must(m -> m
                                                        .term(t -> t
                                                                .field("nation.keyword")
                                                                .value(nation)
                                                        )
                                                )
                                        )
                                )
                                .withSort(
                                        Sort.by(Sort.Direction.DESC, "score")
                                )
                                .withPageable(PageRequest.of(page, size))
                                .build()
                , StatisticUser.class)
                .map(SearchHit::getContent);
    }

    /**
     * 获取所有技术领域
     * @return Flux<String> 所有不重复的技术领域列表
     */
    public Flux<String> getAllDomains() {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(m -> m))
                .withFields(Collections.singletonList("major_domains"))
                .withSort(Sort.by(Sort.Direction.ASC, "major_domains"))
                .withPageable(PageRequest.of(0, 1000))
                .build();
                
        return esOperations.searchForPage(query, StatisticUser.class)
                .expand(searchHits -> {
                    if (searchHits.hasNext()) {
                        return esOperations.searchForPage(
                                NativeQuery.builder()
                                        .withQuery(q -> q.matchAll(m -> m))
                                        .withFields(Collections.singletonList("major_domains"))
                                        .withSort(Sort.by(Sort.Direction.ASC, "major_domains"))
                                        .withPageable(PageRequest.of(searchHits.getNumber() + 1, 1000))
                                        .build(),
                                StatisticUser.class
                        );
                    }
                    return Mono.empty();
                })
                .flatMap(page -> Flux.fromIterable(page.getContent()))
                .flatMap(searchHit -> Flux.fromIterable(
                        searchHit.getContent().getMajorDomains() != null ? 
                        searchHit.getContent().getMajorDomains() : 
                        Collections.emptyList()
                ))
                .filter(domain -> domain != null && !domain.trim().isEmpty())
                .distinct();
    }

    /**
     * 根据技术领域获取GitHub用户排行榜
     * @param domain 技术领域
     * @param page 页码
     * @param size 每页大小
     * @return Flux<StatisticUser> 用户列表（按score降序）
     */
    public Flux<StatisticUser> getRankingByDomain(String domain, int page, int size) {
        return esOperations.search(NativeQuery.builder()
                        .withQuery(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t
                                                        .field("major_domains")
                                                        .value(domain)
                                                )
                                        )
                                )
                        )
                        .withSort(
                                Sort.by(Sort.Direction.DESC, "score")
                        )
                        .withPageable(PageRequest.of(page, size))
                        .build()
                , StatisticUser.class)
                .map(SearchHit::getContent);
    }

}
