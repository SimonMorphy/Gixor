package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.FeignConfig;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.FallbackFactory.RpcUserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 00:45
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:45
 */
@ReactiveFeignClient(
    name = "gixor-system",
    qualifier = "RpcUserService",
    fallbackFactory = RpcUserServiceFallbackFactory.class,
    configuration = FeignConfig.class
)
public interface RpcUserService {
    @PostMapping("/user")
    Mono<Boolean> addUser(@RequestBody User user);

    @GetMapping("/user/exists/{uuid}")
    Mono<Boolean> exists(@PathVariable String uuid);

}
