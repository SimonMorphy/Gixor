package com.cpy3f2.Gixor.Endpoint;
import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.SubscriptionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.*;
import reactor.core.publisher.Mono;
/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 02:01
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 02:01
 */



/**
 * GitHub仓库订阅相关接口
 * @author simon
 * @since 2024/11/4
 */
@Endpoint("/sub")
public class SubscriptionEndpoint {


    @Resource
    private SubscriptionService subscriptionService;


    /**
     * 获取仓库的观察者列表
     */
    @GetExchange("/repo/{owner}/{repo}")
    public Mono<ResponseResult> getRepositoryWatchers(
            @PathVariable String owner,
            @PathVariable String repo) {
        return subscriptionService.getRepositoryWatchers(owner, repo)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取仓库订阅状态
     */
    @GetExchange("/{owner}/{repo}")
    public Mono<ResponseResult> getRepositorySubscription(
            @PathVariable String owner,
            @PathVariable String repo) {
        return subscriptionService.getRepositorySubscription(owner, repo)
                .map(ResponseResult::success);
    }

    /**
     * 设置仓库订阅状态
     */
    @PutExchange("/{owner}/{repo}")
    public Mono<ResponseResult> setRepositorySubscription(
            @PathVariable String owner,
            @PathVariable String repo,
            boolean subscribed,
            boolean ignored) {
        return subscriptionService.setRepositorySubscription(owner, repo, subscribed, ignored)
                .map(ResponseResult::success);
    }

    /**
     * 取消仓库订阅
     */
    @DeleteExchange("/{owner}/{repo}")
    public Mono<ResponseResult> deleteRepositorySubscription(
            @PathVariable String owner,
            @PathVariable String repo) {
        return subscriptionService.deleteRepositorySubscription(owner, repo)
                .then(Mono.just(ResponseResult.success("取消订阅成功")));
    }

    /**
     * 获取认证用户关注的仓库列表
     */
    @GetExchange("/watching")
    public Mono<ResponseResult> getAuthenticatedUserWatching() {
        return subscriptionService.getAuthenticatedUserWatching()
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取指定用户关注的仓库列表
     */
    @GetExchange("/watching/{username}")
    public Mono<ResponseResult> getUserWatching(@PathVariable String username) {
        return subscriptionService.getUserWatching(username)
                .collectList()
                .map(ResponseResult::success);
    }
}