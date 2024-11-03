package com.cpy3f2.Gixor.FallbackFactory;

import com.cpy3f2.Gixor.Service.RpcRoleService;
import com.cpy3f2.Gixor.Service.RpcUserService;
import org.springframework.stereotype.Component;
import reactivefeign.FallbackFactory;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 13:40
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 13:40
 */
@Component
public class RpcRoleServiceFallbackFactory  implements FallbackFactory<RpcRoleService> {
    @Override
    public RpcRoleService apply(Throwable throwable) {
        return id -> Flux.error(throwable);
    }
}
