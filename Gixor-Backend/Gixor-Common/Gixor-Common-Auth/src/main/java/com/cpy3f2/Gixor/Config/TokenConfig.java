package com.cpy3f2.Gixor.Config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import com.cpy3f2.Gixor.Condition.GateWayCondition;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 20:35
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 20:35
 */
@Configuration
public class TokenConfig {

    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        return new SaTokenConfig()
                .setActiveTimeout(-1)
                .setIsConcurrent(false)
                .setIsShare(true)
                .setTokenName("gixor-login")
                .setTokenStyle("uuid")
                .setIsLog(false)
                .setIsReadCookie(false)
                .setIsReadHeader(true);
    }

}
