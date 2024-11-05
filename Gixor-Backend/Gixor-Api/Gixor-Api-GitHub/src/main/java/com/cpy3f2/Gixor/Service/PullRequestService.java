package com.cpy3f2.Gixor.Service;



import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.DTO.PullRequestDTO;
import com.cpy3f2.Gixor.Domain.PullRequest;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 23:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 23:23
 */

@Service
public class PullRequestService {

    @Resource
    private WebClient githubClient;

    public Flux<PullRequest> listPullRequests(String owner, String repo, BaseQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/repos/{owner}/{repo}/pulls"),
                                settings)
                        .build(owner, repo))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取PR列表失败")))
                .bodyToFlux(PullRequest.class);
    }

    public Mono<PullRequest> getPullRequest(String owner, String repo, Integer number) {
        return githubClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{pull_number}",
                        owner, repo, number)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取PR详情失败")))
                .bodyToMono(PullRequest.class);
    }

    public Mono<PullRequest> createPullRequest(String owner, String repo, PullRequestDTO pullRequest) {
        return githubClient.post()
                .uri("/repos/{owner}/{repo}/pulls", owner, repo)
                .bodyValue(pullRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("创建PR失败")))
                .bodyToMono(PullRequest.class);
    }

    public Mono<PullRequest> updatePullRequest(String owner, String repo,
                                               Integer number, PullRequestDTO pullRequest) {
        return githubClient.patch()
                .uri("/repos/{owner}/{repo}/pulls/{pull_number}",
                        owner, repo, number)
                .bodyValue(pullRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("更新PR失败")))
                .bodyToMono(PullRequest.class);
    }
}