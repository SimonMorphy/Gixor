package com.cpy3f2.Gixor.Config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-29 09:29
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-29 09:29
 */
@Configuration
public class SaTokenConfig {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/render", "/callback","/test","/check")
                .setAuth(obj -> StpUtil.checkLogin())
                .setError(e -> ResponseResult.error(e.getMessage()));
    }
}
