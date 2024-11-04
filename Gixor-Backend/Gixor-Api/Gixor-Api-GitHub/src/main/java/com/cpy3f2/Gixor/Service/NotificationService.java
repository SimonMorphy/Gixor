package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.Notification;
import com.cpy3f2.Gixor.Domain.QuerySetting;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 22:45
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 22:45
 */
@Service
public class NotificationService {
    @Resource
    private WebClient githubClient;

    public Flux<Notification> getNotification(QuerySetting querySetting) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(uriBuilder, querySetting)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("获取失败！请稍后再试")))
                .bodyToFlux(Notification.class);
    }
    public Mono<Void> markAsRead() {
        return githubClient
                .put()
                .uri("/notifications")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("已读失败！请稍后再试")))
                .bodyToMono(Void.class);
    }

}
