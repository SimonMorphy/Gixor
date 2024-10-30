package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.FeignConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 00:12
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 00:12
 */

@Service
public class StarringService {

    @Resource
    private WebClient githubClient;

    public Mono<Boolean> isStarred(@PathVariable String owner, @PathVariable String repo) {
        return githubClient
                .get()
                .uri("/user/starred/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

}
