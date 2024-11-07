package com.cpy3f2.Gixor.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-25 12:21
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-25 12:21
 */
@Configuration
public class GlobalTokenFilter {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路径
                .addInclude("/**")
                // 排除静态资源
                .addExclude(
                        "*.html",
                        "*.css",
                        "*.js",
                        "*.ico",
                        "*.png",
                        "*.jpg",
                        "*.gif",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html/**"
                )
                // 排除认证相关接口
                .addExclude(
                        "/auth/**",
                        "/render/**",
                        "/authen/**"
                )
                // 鉴权规则
//                .setAuth(obj -> {
//                    SaRouter.match("/**")
//                            .check(r -> StpUtil.checkLogin());
//
//                    // GitHub相关接口权限
//                    SaRouter.match("/github/**")
//                            .check(r -> StpUtil.checkPermission("github"));
//
//                    // 系统管理接口权限
//                    SaRouter.match("/system/**")
//                            .check(r -> StpUtil.checkPermission("system"));
//
//                    // 权限管理接口
//                    SaRouter.match("/perm/**")
//                            .check(r -> StpUtil.checkPermission("permission"));
//                })
                // 异常处理
                .setError(e-> SaResult.error("未登录"));
    }
}
