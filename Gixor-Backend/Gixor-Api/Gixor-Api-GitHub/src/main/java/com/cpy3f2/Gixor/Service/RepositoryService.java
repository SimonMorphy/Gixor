package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.GitHubRepository;
import com.cpy3f2.Gixor.Domain.QuerySetting;
import com.cpy3f2.Gixor.Domain.TrendyRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-31 17:18
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-31 17:18
 */
@Service
@Slf4j
public class RepositoryService {

    @Resource
    private WebClient githubClient;

    public Flux<TrendyRepository> listTrendyRepos(){
        return WebClient.builder()
                .baseUrl("https://api.gitterapp.com")
                .build()
                .get()
                .retrieve()
                .bodyToFlux(TrendyRepository.class);
    }
    public Flux<GitHubRepository> listStarredRepo(String username, QuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/starred"),
                        querySetting
                ).build(username))
                .retrieve()
                .bodyToFlux(GitHubRepository.class);
    }
    public Flux<GitHubRepository> listStarredRepo(QuerySetting querySetting) {
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/user/starred"),
                        querySetting
                ).build())
                .retrieve()
                .bodyToFlux(GitHubRepository.class);
    }

    public Flux<GitHubRepository> listRepos(QuerySetting querySetting){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/user/repos"),
                                querySetting)
                .build())
                .retrieve()
                .bodyToFlux(GitHubRepository.class);
    }

    public Flux<GitHubRepository> listRepos(String username,QuerySetting querySetting){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/repos"),
                        querySetting)
                .build(username))
                .retrieve()
                .bodyToFlux(GitHubRepository.class);
    }


}