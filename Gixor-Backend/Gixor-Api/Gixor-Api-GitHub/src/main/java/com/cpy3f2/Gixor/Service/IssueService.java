package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.DTO.IssueDTO;
import com.cpy3f2.Gixor.Domain.Issue;
import com.cpy3f2.Gixor.Domain.Query.IssueQuerySetting;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<Issue> listIssues(final IssueQuerySetting settings){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/issues"),
                                settings)
                        .build())
                .retrieve()
                .bodyToFlux(Issue.class);
    }
    public Flux<Issue> listRepoIssues(final String owner, final String repo, final IssueQuerySetting settings){
        return githubClient
                .get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/repos/{owner}/{repo}/issues"),
                                settings)
                        .build(owner,repo))
                .retrieve()
                .bodyToFlux(Issue.class);
    }
    public Mono<Void> createIssues(final String owner,final String repo, final IssueDTO issueDTO){
        return githubClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/issues").build(owner,repo))
                .bodyValue(issueDTO)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("创建失败，请稍后再试")))
                .bodyToMono(Void.class);
    }

    public Mono<Issue> getAnIssue(final String owner, final String repo, final Integer number){
        return githubClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/issues/{number}").build(owner,repo,number))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("获取失败，请稍后再试")))
                .bodyToMono(Issue.class);
    }

    public Mono<Void> updateIssue(final String owner, final String repo, final Integer number, final IssueDTO issueDTO){
        return githubClient
                .patch()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/issues/{number}").build(owner,repo,number))
                .bodyValue(issueDTO)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("更新失败，请稍后再试")))
                .bodyToMono(Void.class);
    }

    public Mono<Void> lockIssue(final String owner, final String repo,final String issueNumber) {
        return githubClient
                .put()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/issues/{issueNumber}/lock").build(owner,repo,issueNumber))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("锁定失败，请稍后再试")))
                .bodyToMono(Void.class);
    }
    public Mono<Void> unlockIssue(final String owner, final String repo,final String issueNumber) {
        return githubClient
                .delete()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/issues/{issueNumber}/lock").build(owner,repo,issueNumber))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("解锁失败，请稍后再试")))
                .bodyToMono(Void.class);
    }
    public Flux<Issue> listAssignedIssues(final IssueQuerySetting settings) {
        return githubClient
                .get()
                .uri(uriBuilder-> GitHubApi.addQueryParams(uriBuilder.path("/user/issues"),settings).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("获取失败，请稍后再试")))
                .bodyToFlux(Issue.class);
    }
}
