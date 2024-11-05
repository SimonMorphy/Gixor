package com.cpy3f2.Gixor.Handler;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.LoginService;
import com.cpy3f2.Gixor.Service.RpcUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-27 14:04
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-27 14:04
 */
@RestController
@Slf4j
public class LoginHandler {

    @Resource
    private AuthRequest authGithubRequest;


    @Resource
    private LoginService loginService;

    @Resource
    RpcUserService userService;


    public Mono<ServerResponse> render(ServerRequest request){
        return ServerResponse
                .status(HttpStatus.FOUND)
                .location(URI.create(authGithubRequest.authorize(AuthStateUtils.createState())))
                .build();
    }

    @GetExchange("/callback")
    public Mono<ResponseResult> login(AuthCallback callback) {
        return Mono.just(authGithubRequest.login(callback).getData())
                .cast(AuthUser.class)
                .switchIfEmpty(Mono.error(new AuthException("GitHub授权失败：用户数据为空")))
                .flatMap(loginService::login);
    }

//    public Mono<ServerResponse> login(){
//
//    }



}
