package com.cpy3f2.Gixor.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.cpy3f2.Gixor.Domain.ResponseResult;
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
                .addExclude("*.html","*.css","*.js")
                .addExclude("/auth/**")
                .setError(e-> SaResult.error("未登录")
                );
    }
}
