package com.cpy3f2.Gixor.Router;

import com.cpy3f2.Gixor.Handler.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-27 13:51
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-27 13:51
 */
@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(LoginHandler handler) {
        return RouterFunctions.route()
                .GET("/render", handler::render)
                .GET("/test",handler::test)
                .build();
    }

}
