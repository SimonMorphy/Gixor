package com.cpy3f2.Gixor.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.SimpleUser;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Exception.constant.GitHubErrorCodes;
import com.cpy3f2.Gixor.Exception.user.UserOperationException;
import com.cpy3f2.Gixor.Exception.util.GitHubErrorMessageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return githubUser == null || githubUser.getGithubId() == null?
                Mono.just(githubUser):
                esOperations.search(
                        NativeQuery.builder()
                                .withQuery(q -> q
                                        .term(t -> t
                                                .field("id")
                                                .value(githubUser.getGithubId())))
                                .build()
                , GitHubUser.class)
                .map(SearchHit::getContent)
                .next()
                .map(statsUser -> {
                    BeanUtil.copyProperties(statsUser, githubUser, CopyOptions.create().setIgnoreNullValue(true));
                    return githubUser;
                })
                .defaultIfEmpty(githubUser);
    }



    /**
     * 获取GitHub用户排行榜
     * @param page 页码
     * @param size 每页大小
     * @return Flux<GitHubUser> 用户列表（按score降序）
     */
    public Flux<GitHubUser> getRanking(int page, int size) {
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
                GitHubUser.class
        )
                .map(SearchHit::getContent)
                .flatMap(esUser -> getGitHubUser(esUser.getLogin())
                        .map(githubUser -> {
                            BeanUtil.copyProperties(esUser, githubUser, CopyOptions.create().setIgnoreNullValue(true));
                            return githubUser;
                        }));
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

}
