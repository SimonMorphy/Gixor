package com.cpy3f2.Gixor.Endpoint;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.TrendyUser;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Service.GitHubUserService;
import com.cpy3f2.Gixor.Service.UserService;
import com.cpy3f2.Gixor.service.CacheService;
import jakarta.annotation.Resource;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 14:21
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 14:21
 */
@Endpoint("/user")
public class UserEndpoint {

    @Resource
    private GitHubUserService gitUserService;


    @Resource
    private UserService userService;


    @Resource
    private CacheService cacheService;







    @PostMapping
    @SaCheckRole(Constants.ADMIN)
    public Mono<Boolean> addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/exists/{uuid}")
    public Mono<Boolean> exists(@PathVariable String uuid) {
        return userService.exists(uuid);
    }

    @GetMapping
    public Mono<ResponseResult> getInfo()
    {
        return gitUserService.getInfo()
                .map(ResponseResult::success);
    }

    @GetMapping("/trendy")
    public Mono<ResponseResult> list(){
        return cacheService.getCacheObjectFlux(Constants.TRENDY_USER_KEY, TrendyUser.class)
                .collectList()
                .map(ResponseResult::success);
    }
    @GetMapping("/rank")
    public Mono<ResponseResult> list(int page,int pageSize){
        return cacheService.getCacheList(Constants.RANK_KEY, GitHubUser.class,page,pageSize)
                .collectList()
                .map(ResponseResult::success);
    }
    @GetMapping("/{username}")
    public Mono<ResponseResult> getInfo(@PathVariable String username)
    {
        return gitUserService.getInfo(username)
                .map(ResponseResult::success);
    }

}
