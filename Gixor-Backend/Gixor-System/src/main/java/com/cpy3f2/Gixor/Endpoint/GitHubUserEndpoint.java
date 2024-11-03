package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Service.GitHubUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 14:21
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 14:21
 */
@Endpoint("/users")
public class GitHubUserEndpoint {

    @Resource
    private GitHubUserService userService;



    @GetExchange
    public Mono<GitHubUser> getInfo()
    {
        return userService.getInfo();
    }

    @GetExchange("/{username}")
    public Mono<GitHubUser> getInfo(@PathVariable String username)
    {
        return userService.getInfo(username);
    }

}
