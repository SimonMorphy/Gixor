package com.cpy3f2.Gixor.Condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-31 09:09
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-31 09:09
 */
@Component
public class GateWayCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String applicationName = context.getEnvironment().getProperty("spring.application.name");
        return applicationName != null && !"gixor-gateway".equals(applicationName);
    }
}
