package com.cpy3f2.Gixor.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
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

    public Mono<GitHubUser> getInfo(){
        return githubClient
                .get()
                .uri("/user")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取GitHub用户信息失败")))
                .bodyToMono(GitHubUser.class)
                .flatMap(this::getStats)
                .doOnSuccess(user -> log.info("Successfully retrieved user info for: {}", user.getLogin()))
                .doOnError(error -> log.error("Error retrieving user info: {}", error.getMessage()));
    }

    public Mono<GitHubUser> getGitHubUser(String username) {
        return gitHubClientWithoutToken
                .get()
                .uri("/users/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取GitHub用户信息失败")))
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
    public Flux<GitHubUser> getFollowers(BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/user/followers"),
                        querySetting
                ).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取GitHub用户信息失败"))
                        )
                .bodyToFlux(GitHubUser.class);
    }
    public Flux<GitHubUser> getFollowing(BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/user/following"),
                        querySetting
                ).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取GitHub用户信息失败"))
                )
                .bodyToFlux(GitHubUser.class);
    }
    public Mono<Void> checkFollowed(String username){
        return githubClient
                .get()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("未关注该用户"))
                )
                .bodyToMono(Void.class);
    }
    public Mono<Void> follow(String username){
        return githubClient
                .put()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("关注失败"))
                )
                .bodyToMono(Void.class);
    }
    public Mono<Void> unfollow(String username){
        return githubClient
                .delete()
                .uri("/user/following/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("取消关注失败"))
                )
                .bodyToMono(Void.class);
    }
    public Flux<GitHubUser> listFollowers(String username, BaseQuerySetting querySetting) {
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
                .bodyToFlux(GitHubUser.class);
    }
    public Flux<GitHubUser> listFollowing(String username, BaseQuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/following"),
                        querySetting
                ).build(username))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("搜索失败"))
               ).bodyToFlux(GitHubUser.class);
    }

}
