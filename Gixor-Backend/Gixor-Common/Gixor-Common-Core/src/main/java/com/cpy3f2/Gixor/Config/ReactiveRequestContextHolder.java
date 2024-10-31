package com.cpy3f2.Gixor.Config;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-28 14:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-28 14:23
 */
@Component
public class ReactiveRequestContextHolder {
    private final ThreadLocal<ServerWebExchange> exchange = new ThreadLocal<>();
    
    public void set(ServerWebExchange serverWebExchange) {
        exchange.set(serverWebExchange);
    }
    
    public ServerWebExchange get() {
        return exchange.get();
    }
    
    public void clear() {
        exchange.remove();
    }
}
