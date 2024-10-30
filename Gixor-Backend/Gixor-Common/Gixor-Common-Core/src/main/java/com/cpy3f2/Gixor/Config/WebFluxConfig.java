package com.cpy3f2.Gixor.Config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 21:31
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 21:31
 */
@Configuration
public class WebFluxConfig {
    
    @Resource
    private ReactiveRequestContextHolder contextHolder;

    @Component
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public class ReactiveRequestContextFilter implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            contextHolder.set(exchange);
            return chain.filter(exchange)
                    .doFinally(signalType -> contextHolder.clear());
        }
    }
}
