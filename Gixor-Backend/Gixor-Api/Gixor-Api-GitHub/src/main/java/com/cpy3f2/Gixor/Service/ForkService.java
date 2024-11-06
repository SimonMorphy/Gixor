package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.DTO.ForkDTO;
import com.cpy3f2.Gixor.Domain.GitHubRepository;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.constant.GitHubErrorCodes;
import com.cpy3f2.Gixor.Exception.repository.RepositoryOperationException;
import com.cpy3f2.Gixor.Exception.user.UserOperationException;
import com.cpy3f2.Gixor.Exception.util.GitHubErrorMessageUtil;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 19:29
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 19:29
 */
@Service
public class ForkService {
    @Resource
    private WebClient githubClient;
    @Resource
    private GitHubErrorMessageUtil errorMessageUtil;

    public Flux<GitHubRepository.Owner> listForks(String owner, String repo, BaseQuerySetting setting) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/repos/{owner}/{repo}/forks"),
                                setting)
                        .build(owner, repo))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new UserOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        )))
                .bodyToFlux(GitHubRepository.class)
                .map(GitHubRepository::getOwner);
    }
    public Mono<GitHubRepository> fork(String owner, String repo, ForkDTO fork){
        return githubClient.post()
                .uri("/repos/{owner}/{repo}/forks", owner, repo)
                .bodyValue(fork)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RepositoryOperationException(
                                errorMessageUtil.getMessage(GitHubErrorCodes.USER_INFO_GET),
                                new ResponseResult()
                        ))
                )
                .bodyToMono(GitHubRepository.class);
    }

}
