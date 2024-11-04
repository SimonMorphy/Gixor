package com.cpy3f2.Gixor.config;

import cn.dev33.satoken.stp.StpInterface;
import jakarta.annotation.Resource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 13:35
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 13:35
 */
@Component
public class AuthInterface implements StpInterface {

    @Resource
    private WebClient webClient;



    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return webClient.get()
                .uri("http://gixor-system/role/{uuid}", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return webClient.get()
                .uri("http://gixor-system/perm/{uuid}", loginId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
