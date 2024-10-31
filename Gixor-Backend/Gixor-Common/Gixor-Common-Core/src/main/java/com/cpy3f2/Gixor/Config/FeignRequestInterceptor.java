package com.cpy3f2.Gixor.Config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 20:43
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 20:43
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    @Resource
    private ReactiveRequestContextHolder contextHolder;
    
    @Override
    public void apply(RequestTemplate requestTemplate) {
            ServerWebExchange exchange = contextHolder.get();
            if (exchange != null) {
                exchange.getRequest().getHeaders().forEach((name, values) -> 
                    requestTemplate.header(name, values.toArray(new String[0])));
            }
    }
}
