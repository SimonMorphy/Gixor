package com.cpy3f2.Gixor.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson2.util.BeanUtils;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
@RequiredArgsConstructor
public class GitHubUserService {

    @Resource
    private WebClient githubClient;

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

    /**
     * 根据用户名获取用户信息并补充统计数据
     */
    public Mono<GitHubUser> getInfo(String username) {
        return githubClient
                .get()
                .uri("/users/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取GitHub用户信息失败")))
                .bodyToMono(GitHubUser.class)
                .flatMap(this::getStats)
                .doOnSuccess(user -> log.info("Successfully retrieved user info for: {}", username))
                .doOnError(error -> log.error("Error retrieving user info for {}: {}", username, error.getMessage()));
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
                .defaultIfEmpty(githubUser)
                .onErrorResume(error -> {
                    log.error("Error fetching stats, returning original user data: {}", error.getMessage());
                    return Mono.just(githubUser);
                });
    }

}
