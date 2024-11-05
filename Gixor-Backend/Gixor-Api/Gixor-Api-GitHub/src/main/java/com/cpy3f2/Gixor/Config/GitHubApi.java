package com.cpy3f2.Gixor.Config;

import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.util.UriBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 11:08
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 11:08
 */
@Configuration
@Slf4j
public class GitHubApi {

    @Value("${admin.token}")
    private String adminToken;

    @Bean
    public WebClient.Builder gitHubClientBuilder() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .filter((request, next)-> {
                    String token = StpUtil.getTokenValue();
                    if (token != null) {
                        request = ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build();
                    }
                    return next.exchange(request);
                });
    }
    @Bean
    public WebClient.Builder gitHubClientBuilderWithoutToken() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken);
    }

    /**
     * 使用反射动态添加查询参数
     * @param builder UriBuilder实例
     * @param querySetting 查询设置
     * @return 更新后的UriBuilder
     */
    public  static  UriBuilder addQueryParams(UriBuilder builder,  BaseQuerySetting querySetting) {
        if (querySetting == null) {
            return builder;
        }

        try {
            // 获取当前类及其所有父类的字段
            Class<?> currentClass = querySetting.getClass();
            while (currentClass != null && currentClass != Object.class) {
                Field[] fields = currentClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(querySetting);
                    
                    if (value != null) {
                        String paramName = getParamName(field);
                        String paramValue = formatValue(value);
                        
                        if (paramValue != null) {
                            builder.queryParam(paramName, paramValue);
                        }
                    }
                }
                currentClass = currentClass.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            log.error("反射获取QuerySetting属性失败", e);
        }
        
        return builder;
    }
    
    /**
     * 获取参数名称，优先使用JsonProperty注解的值
     */
    private static String getParamName(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        return jsonProperty != null ? jsonProperty.value() : field.getName();
    }
    
    /**
     * 格式化参数值
     */
    private static String formatValue(Object value) {
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ISO_DATE_TIME);
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    @Bean
    @Primary
    public WebClient gitHubClient(WebClient.Builder gitHubClientBuilder) {
        return gitHubClientBuilder.build();
    }
    @Bean
    public WebClient gitHubClientWithoutToken(WebClient.Builder gitHubClientBuilderWithoutToken) {
        return gitHubClientBuilderWithoutToken.build();
    }

}
