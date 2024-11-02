package com.cpy3f2.Gixor.Service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.User;
import jakarta.annotation.Resource;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-29 08:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-29 08:25
 */
@Service
public class LoginService {

    @Resource
    private RpcUserService userService;
    public Mono<ResponseResult> login(AuthUser authuser){
        return Mono.fromCallable(() -> {
            StpUtil.login(authuser.getUuid(),
                    new SaLoginModel()
                            .setDevice(Constants.ANDROID)
                            .setIsWriteHeader(Constants.TRUE)
                            .setToken(authuser.getToken().getAccessToken())
                            .setTimeout(Constants.CACHE_EXPIRATION)
            );
            return StpUtil.getTokenInfo();
        }).flatMap(tokenValue -> 
            userService.exists(authuser.getUuid())
                .flatMap(exist -> {
                    if (!exist) {
                        return userService.addUser(User.builder()
                                .id(Long.parseLong(authuser.getUuid()))
                                .nickname(authuser.getNickname())
                                .username(authuser.getUsername())
                                .avatar(authuser.getAvatar())
                                .email(authuser.getEmail())
                                .build())
                                .then(Mono.just(false));
                    }
                    return Mono.just(true);
                })
                .map(s -> ResponseResult.success("登录成功！").put(Constants.TOKEN, tokenValue))
        );
    }
}