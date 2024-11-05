package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.GitHubUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 22:29
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 22:29
 */
@Endpoint("/follow")
public class FollowEndpoint {

    @Resource
    private GitHubUserService gitHubUserService;

    /**
     * 获取当前用户的关注者列表
     */
    @GetMapping("/followers")
    public Mono<ResponseResult> getFollowers(BaseQuerySetting settings) {
        return gitHubUserService.getFollowers(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取当前用户关注的用户列表
     */
    @GetMapping("/following") 
    public Mono<ResponseResult> getFollowing(BaseQuerySetting settings) {
        return gitHubUserService.getFollowing(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 检查是否关注了指定用户
     */
    @GetMapping("/{username}")
    public Mono<ResponseResult> checkFollowed(@PathVariable String username) {
        return gitHubUserService.checkFollowed(username)
                .then(Mono.just(ResponseResult.success("已关注该用户")));
    }

    /**
     * 关注指定用户
     */
    @PutExchange("/{username}")
    public Mono<ResponseResult> follow(@PathVariable String username) {
        return gitHubUserService.follow(username)
                .then(Mono.just(ResponseResult.success("关注成功")));
    }

    /**
     * 取消关注指定用户
     */
    @DeleteMapping("/{username}")
    public Mono<ResponseResult> unfollow(@PathVariable String username) {
        return gitHubUserService.unfollow(username)
                .then(Mono.just(ResponseResult.success("取消关注成功")));
    }

    /**
     * 获取指定用户的关注者列表
     */
    @GetMapping("/{username}/followers")
    public Mono<ResponseResult> listFollowers(@PathVariable String username, BaseQuerySetting settings) {
        return gitHubUserService.listFollowers(username, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取指定用户关注的用户列表
     */
    @GetMapping("/{username}/following")
    public Mono<ResponseResult> listFollowing(@PathVariable String username, BaseQuerySetting settings) {
        return gitHubUserService.listFollowing(username, settings)
                .collectList()
                .map(ResponseResult::success);
    }
}
