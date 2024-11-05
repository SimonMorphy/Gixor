package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;


import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.DTO.PullRequestDTO;
import com.cpy3f2.Gixor.Domain.PullRequest;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.PullRequestService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 23:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 23:23
 */

@Endpoint("/pr")
public class PullRequestEndpoint {

    @Resource
    private PullRequestService prService;

    /**
     * 获取仓库PR列表
     */
    @GetExchange("/{owner}/{repo}")
    public Mono<ResponseResult> listPullRequests(@PathVariable String owner,
                                                 @PathVariable String repo,
                                                 BaseQuerySetting settings) {
        return prService.listPullRequests(owner, repo, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取单个PR详情
     */
    @GetExchange("/{owner}/{repo}/{number}")
    public Mono<ResponseResult> getPullRequest(@PathVariable String owner,
                                               @PathVariable String repo,
                                               @PathVariable Integer number) {
        return prService.getPullRequest(owner, repo, number)
                .map(ResponseResult::success);
    }

    /**
     * 创建PR
     */
    @PostExchange("/{owner}/{repo}")
    public Mono<ResponseResult> createPullRequest(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @RequestBody PullRequestDTO pullRequest) {
        return prService.createPullRequest(owner, repo, pullRequest)
                .map(pr -> ResponseResult.success("PR创建成功"));
    }

    /**
     * 更新PR
     */
    @PatchExchange("/{owner}/{repo}/{number}")
    public Mono<ResponseResult> updatePullRequest(@PathVariable String owner,
                                                  @PathVariable String repo,
                                                  @PathVariable Integer number,
                                                  @RequestBody PullRequestDTO pullRequest) {
        return prService.updatePullRequest(owner, repo, number, pullRequest)
                .map(pr -> ResponseResult.success("PR更新成功"));
    }
}