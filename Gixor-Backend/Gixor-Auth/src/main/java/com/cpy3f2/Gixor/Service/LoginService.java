package com.cpy3f2.Gixor.Service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.User;
import jakarta.annotation.Resource;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
        StpUtil.login(authuser.getUuid(),
                new SaLoginModel()
                        .setDevice(Constants.ANDROID)
                        .setIsWriteHeader(Constants.TRUE)
                        .setToken(authuser.getToken().getAccessToken())
                        .setTimeout(Constants.CACHE_EXPIRATION)
        );
        String tokenValue = StpUtil.getTokenValue();
        return Mono.just(authuser)
        .flatMap(user -> userService.exists(user.getUuid())
            .flatMap(exist -> {
                if (!exist) {
                    User sysUser = new User();
                    sysUser.setId(Long.parseLong(user.getUuid()));
                    sysUser.setNickName(user.getNickname());
                    sysUser.setUsername(user.getUsername());
                    sysUser.setAvatar(user.getAvatar());
                    sysUser.setEmail(user.getEmail());
                    userService.addUser(sysUser);
                }
                return Mono.just(ResponseResult.success("登录成功！").put(Constants.TOKEN, tokenValue));
            }));
    }
}