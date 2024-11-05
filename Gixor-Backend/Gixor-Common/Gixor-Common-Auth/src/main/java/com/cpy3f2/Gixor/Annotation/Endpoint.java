package com.cpy3f2.Gixor.Annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.HttpExchange;

import java.lang.annotation.*;

/**
 * @author : simon
 * @description : 端点注解，标注一个面向外界用于交换数据的端口
 * @last : 2024-10-30 15:12
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 15:12
 */
@HttpExchange
@Target(ElementType.TYPE)
@RestController
@Validated
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Endpoint {
    @AliasFor(annotation = HttpExchange.class, attribute = "url")
    String value() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "url")
    String url() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "method")
    String method() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "contentType")
    String contentType() default "";

    @AliasFor(annotation = HttpExchange.class, attribute = "accept")
    String[] accept() default {};
}
