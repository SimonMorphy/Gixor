package com.cpy3f2.Gixor.Handler;

import cn.dev33.satoken.stp.StpUtil;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Service.RpcPermissionService;
import com.cpy3f2.Gixor.Service.RpcRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 14:47
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 14:47
 */
@RestController
@Slf4j
public class AuthHandler {
    @Resource
    private RpcRoleService rpcRoleService;

    @Resource
    private RpcPermissionService rpcPermissionService;

    public Mono<ServerResponse> getRoleList(ServerRequest request) {
        return rpcRoleService.getRoleList((
                Long.parseLong((String) StpUtil.getLoginIdByToken(request.headers()
                        .firstHeader(Constants.TOKEN_KEY)))))
                .collectList()
                .flatMap(roleList -> ServerResponse.ok().bodyValue(roleList));
    }
    public Mono<ServerResponse> getPermissionList(ServerRequest request) {
        return rpcPermissionService.getPermissionList((
                Long.parseLong((String) StpUtil.getLoginIdByToken(request.headers()
                        .firstHeader(Constants.TOKEN_KEY)))))
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

}
