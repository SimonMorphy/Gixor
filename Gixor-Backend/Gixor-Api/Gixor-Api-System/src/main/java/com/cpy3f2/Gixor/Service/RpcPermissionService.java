package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.FeignConfig;
import com.cpy3f2.Gixor.FallbackFactory.RpcPermissionServiceFallbackFactory;
import com.cpy3f2.Gixor.FallbackFactory.RpcUserServiceFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 13:40
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 13:40
 */
@ReactiveFeignClient(
        name = "gixor-system",
        qualifier = "RpcPermissionService",
        fallbackFactory = RpcPermissionServiceFallbackFactory.class,
        configuration = FeignConfig.class
)
public interface RpcPermissionService {

    /**
     * 获取权限列表
     * @param id 角色ID
     * @return Flux<String>
     * @author simon
     * @since 2024/11/3
     */
    @GetMapping("/perm/{id}")
    Flux<String> getPermissionList(@PathVariable Long id);
}
