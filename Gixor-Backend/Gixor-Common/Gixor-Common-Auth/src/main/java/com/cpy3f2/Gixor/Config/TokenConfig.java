package com.cpy3f2.Gixor.Config;

import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.context.annotation.Bean;
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
        SaTokenConfig config = new SaTokenConfig();
        config.setTimeout(30 * 24 * 60 * 60);
        config.setActiveTimeout(-1);
        config.setIsConcurrent(true);
        config.setIsShare(true);
        config.setTokenStyle("uuid");
        config.setIsLog(false);
        return config;
    }

}
