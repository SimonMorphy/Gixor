package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.Issue;
import com.cpy3f2.Gixor.Domain.QuerySetting;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 00:38
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 00:38
 */
@Service
public class IssueService {
    @Resource
    private WebClient githubClient;

    public Flux<Issue> listIssues(QuerySetting settings){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/issues"),
                                settings)
                        .build())
                .retrieve()
                .bodyToFlux(Issue.class);
    }
    public Flux<Issue> listRepoIssues(String owner, String repo, QuerySetting settings){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/repos/{owner}/{repo}/issues"),
                                settings)
                        .build(owner,repo))
                .retrieve()
                .bodyToFlux(Issue.class);
    }


}
