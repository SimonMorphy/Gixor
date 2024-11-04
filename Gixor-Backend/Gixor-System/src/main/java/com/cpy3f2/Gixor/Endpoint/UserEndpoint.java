package com.cpy3f2.Gixor.Endpoint;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Service.GitHubUserService;
import com.cpy3f2.Gixor.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

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



    @PostExchange
    @SaCheckRole(Constants.ADMIN)
    public Mono<Boolean> addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetExchange("/exists/{uuid}")
    public Mono<Boolean> exists(@PathVariable String uuid) {
        return userService.exists(uuid);
    }

    @GetExchange
    public Mono<GitHubUser> getInfo()
    {
        return gitUserService.getInfo();
    }

    @GetExchange("/{username}")
    public Mono<GitHubUser> getInfo(@PathVariable String username)
    {
        return gitUserService.getInfo(username);
    }

}
