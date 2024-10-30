package com.cpy3f2.Gixor.FallbackFactory;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Service.RpcUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactivefeign.FallbackFactory;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:03
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:03
 */
@Component
public class RpcUserServiceFallbackFactory implements FallbackFactory<RpcUserService> {

    @Override
    public RpcUserService apply(Throwable throwable) {
        return new RpcUserService() {


            @Override
            public Mono<Boolean> addUser(User user) {
                return Mono.empty();
            }

            @Override
            public Mono<Boolean> exists(String uuid) {
                return Mono.just(false);
            }
        };
    }
}