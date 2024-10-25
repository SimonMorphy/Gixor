package com.cpy3f2.Gixor.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-25 12:21
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-25 12:21
 */
@Configuration
public class GlobalTokenFilter {

    @Bean
    public SaReactorFilter getSaReactorFilter(){
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico","/auth/login**","/auth/render")
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/auth/login
                    SaRouter.match("/**",r-> StpUtil.checkLogin())
                            .notMatch("/auth/login");
                });

    }
}
