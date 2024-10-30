package com.cpy3f2.Gixor.Config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 20:49
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 20:49
 */
@Configuration
public class FeignAutoConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return new FeignRequestInterceptor();
    }
}
