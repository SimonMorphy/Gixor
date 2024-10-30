package com.cpy3f2.Gixor.Config;

import com.xkcoding.http.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.*;
import me.zhyd.oauth.enums.scope.AuthGithubScope;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthScopeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 22:07
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 22:07
 */
@Configuration
@Slf4j
public class LoginRequestConfig {

    @Value("${JustAuth.github.clientId}")
    private String clientId;
    @Value("${JustAuth.github.clientSecret}")
    private String clientSecret;
    @Value("${JustAuth.github.redirectUri}")
    private String redirectUri;

    @Bean
    public AuthRequest authGithubRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .httpConfig(HttpConfig.builder()
                        .timeout(3000)
                        .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210)))
                        .build())
                .scopes(AuthScopeUtils.getScopes(
                        AuthGithubScope.USER,AuthGithubScope.PUBLIC_REPO,
                        AuthGithubScope.GIST,AuthGithubScope.READ_USER
                ))
                .build());

    }

}
