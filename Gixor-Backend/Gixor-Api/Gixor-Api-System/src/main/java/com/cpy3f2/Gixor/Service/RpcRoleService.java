package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.FeignConfig;
import com.cpy3f2.Gixor.FallbackFactory.RpcRoleServiceFallbackFactory;
import com.cpy3f2.Gixor.FallbackFactory.RpcUserServiceFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
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
        qualifier = "RpcRoleService",
        fallbackFactory = RpcRoleServiceFallbackFactory.class,
        configuration = FeignConfig.class
)
public interface RpcRoleService {
    /**
     * 根据用户ID获取角色列表
     * @param id 用户ID
     * @return Flux<String>
     * @author simon
     * @since 2024/11/3
     */

    @GetMapping("/role/{id}")
    Flux<String> getRoleList(@PathVariable Long id);

}
