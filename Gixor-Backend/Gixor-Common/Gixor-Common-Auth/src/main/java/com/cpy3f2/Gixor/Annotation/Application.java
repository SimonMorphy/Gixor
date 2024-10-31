package com.cpy3f2.Gixor.Annotation;

import com.cpy3f2.Gixor.Config.FeignAutoConfiguration;
import com.cpy3f2.Gixor.Config.SerializerConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 19:39
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 19:39
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableR2dbcAuditing
@Inherited
@EnableAspectJAutoProxy(exposeProxy = true)
@Import({FeignAutoConfiguration.class, SerializerConfig.class})
@SpringBootApplication
public @interface Application {
}
