package com.cpy3f2.Gixor.Annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.lang.annotation.*;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 19:38
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 19:38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableReactiveFeignClients
public @interface RemoteDiscovery {
    String[] value() default {};

    String[] basePackages() default { "com.cpy3f2" };

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
