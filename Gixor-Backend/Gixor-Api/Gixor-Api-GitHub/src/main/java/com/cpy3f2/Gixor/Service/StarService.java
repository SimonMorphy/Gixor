package com.cpy3f2.Gixor.Service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 00:12
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 00:12
 */

@Service
@Slf4j
public class StarService {

    @Resource
    private WebClient githubClient;

    public Mono<Void> isStarred(String owner,String repo) {
        return githubClient
                .get()
                .uri("/user/starred/{owner}/{repo}", owner, repo)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("未收藏")))
                .bodyToMono(Void.class);
    }
    public Mono<Void> star(String owner,String repo) {
        return githubClient
                .put()
                .uri("/user/starred/{owner}/{repo}", owner, repo)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("已收藏，无法继续收藏")))
                .bodyToMono(Void.class);
    }

    public Mono<Void> unStar(String owner,String repo) {
        return githubClient
                .delete()
                .uri("/user/starred/{owner}/{repo}", owner, repo)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("未收藏，无法继续取消收藏")))
                .bodyToMono(Void.class);
    }


}
