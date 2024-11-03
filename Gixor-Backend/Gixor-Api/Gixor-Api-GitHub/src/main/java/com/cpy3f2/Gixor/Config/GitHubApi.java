package com.cpy3f2.Gixor.Config;

import com.cpy3f2.Gixor.Domain.QuerySetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import lombok.extern.slf4j.Slf4j;
import cn.dev33.satoken.context.SaTokenContext;

import java.util.Optional;

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

    /**
     * 添加通用查询参数
     * @param builder UriBuilder实例
     * @param querySetting 查询设置
     * @return 更新后的UriBuilder
     */
    public static UriBuilder addQueryParams(UriBuilder builder, QuerySetting querySetting) {
        if (querySetting != null) {
            Optional.ofNullable(querySetting.getSort())
                .ifPresent(sort -> builder.queryParam("sort", sort));

            Optional.ofNullable(querySetting.getDirection())
                .ifPresent(direction -> builder.queryParam("direction", direction));

            Optional.ofNullable(querySetting.getPerPage())
                .ifPresent(perPage -> builder.queryParam("perPage", perPage));

            Optional.ofNullable(querySetting.getPage())
                .ifPresent(page -> builder.queryParam("page", page));
        }
        return builder;
    }

    @Bean
    public WebClient gitHubClient(WebClient.Builder builder) {
        return builder.build();
    }
}
