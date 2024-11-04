package com.cpy3f2.Gixor.Config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import com.cpy3f2.Gixor.Condition.GateWayCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-29 09:29
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-29 09:29
 */
@Configuration
@Conditional(GateWayCondition.class)
public class SaTokenConfig {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter();
    }
}
