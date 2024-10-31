package com.cpy3f2.Gixor.Config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 20:55
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 20:55
 */
@Configuration
public class SerializerConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

}
