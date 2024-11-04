package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Domain.DTO.SubscriptionDTO;
import com.cpy3f2.Gixor.Domain.Record.SubscriptionRequest;
import com.cpy3f2.Gixor.Domain.Watcher;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-01 01:31
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-01 01:31
 */
@Service
public class SubscriptionService {
    @Resource
    private WebClient githubClient;

    /**
     * 获取仓库的观察者列表
     */
    public Flux<Watcher> getRepositoryWatchers(String owner, String repo) {
        return githubClient.get()
                .uri("/repos/{owner}/{repo}/subscribers", owner, repo)
                .retrieve()
                .bodyToFlux(Watcher.class);
    }

    /**
     * 获取当前用户的仓库订阅状态
     */
    public Mono<SubscriptionDTO> getRepositorySubscription(String owner, String repo) {
        return githubClient.get()
                .uri("/repos/{owner}/{repo}/subscription", owner, repo)
                .retrieve()
                .bodyToMono(SubscriptionDTO.class);
    }

    /**
     * 设置仓库订阅状态
     */
    public Mono<SubscriptionDTO> setRepositorySubscription(String owner, String repo,
                                                           boolean subscribed, boolean ignored) {
        return githubClient.put()
                .uri("/repos/{owner}/{repo}/subscription", owner, repo)
                .bodyValue(new SubscriptionRequest(subscribed, ignored))
                .retrieve()
                .bodyToMono(SubscriptionDTO.class);
    }

    /**
     * 取消仓库订阅
     */
    public Mono<Void> deleteRepositorySubscription(String owner, String repo) {
        return githubClient.delete()
                .uri("/repos/{owner}/{repo}/subscription", owner, repo)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * 获取认证用户关注的仓库列表
     */
    public Flux<Watcher> getAuthenticatedUserWatching() {
        return githubClient.get()
                .uri("/user/subscriptions")
                .retrieve()
                .bodyToFlux(Watcher.class);
    }

    /**
     * 获取指定用户关注的仓库列表
     */
    public Flux<Watcher> getUserWatching(String username) {
        return githubClient.get()
                .uri("/users/{username}/subscriptions", username)
                .retrieve()
                .bodyToFlux(Watcher.class);
    }
}
