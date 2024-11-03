package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.DTO.IssueDTO;
import com.cpy3f2.Gixor.Domain.QuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.IssueService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description : Issue相关接口
 * @last : 2024-10-30 18:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 18:25
 */
@Endpoint("/issue")
public class IssueEndpoint {

    @Resource
    private IssueService issueService;

    /**
     * 获取Issue列表
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetExchange
    public Mono<ResponseResult> listIssues(QuerySetting settings) {
        return issueService.listIssues(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取仓库Issue列表
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetExchange("/{owner}/{repo}")
    public Mono<ResponseResult> listRepoIssues(@PathVariable String owner, 
                                             @PathVariable String repo,
                                             QuerySetting settings) {
        return issueService.listRepoIssues(owner, repo, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 创建Issue
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param issueDTO Issue信息
     * @return Mono<ResponseResult>
     */
    @PostExchange("/{owner}/{repo}")
    public Mono<ResponseResult> createIssue(@PathVariable String owner,
                                          @PathVariable String repo,
                                          @RequestBody IssueDTO issueDTO) {
        return issueService.createIssues(owner, repo, issueDTO)
                .then(Mono.just(ResponseResult.success("创建成功")));
    }

    /**
     * 获取单个Issue详情
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param number Issue编号
     * @return Mono<ResponseResult>
     */
    @GetExchange("/{owner}/{repo}/{number}")
    public Mono<ResponseResult> getIssue(@PathVariable String owner,
                                       @PathVariable String repo,
                                       @PathVariable Integer number) {
        return issueService.getAnIssue(owner, repo, number)
                .map(ResponseResult::success);
    }

    /**
     * 更新Issue
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param number Issue编号
     * @param issueDTO Issue更新信息
     * @return Mono<ResponseResult>
     */
    @PatchExchange("/{owner}/{repo}/{number}")
    public Mono<ResponseResult> updateIssue(@PathVariable String owner,
                                          @PathVariable String repo,
                                          @PathVariable Integer number,
                                          @RequestBody IssueDTO issueDTO) {
        return issueService.updateIssue(owner, repo, number, issueDTO)
                .then(Mono.just(ResponseResult.success("更新成功")));
    }

    /**
     * 锁定Issue
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param issueNumber Issue编号
     * @return Mono<ResponseResult>
     */
    @PutExchange("/{owner}/{repo}/{issueNumber}")
    public Mono<ResponseResult> lockIssue(@PathVariable String owner,
                                        @PathVariable String repo,
                                        @PathVariable String issueNumber) {
        return issueService.lockIssue(owner, repo, issueNumber)
                .then(Mono.just(ResponseResult.success("锁定成功")));
    }

    /**
     * 解锁Issue
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param issueNumber Issue编号
     * @return Mono<ResponseResult>
     */
    @DeleteExchange("/{owner}/{repo}/{issueNumber}")
    public Mono<ResponseResult> unlockIssue(@PathVariable String owner,
                                          @PathVariable String repo,
                                          @PathVariable String issueNumber) {
        return issueService.unlockIssue(owner, repo, issueNumber)
                .then(Mono.just(ResponseResult.success("解锁成功")));
    }

    /**
     * 获取分配给当前用户的Issue列表
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetExchange("/assigned")
    public Mono<ResponseResult> listAssignedIssues(QuerySetting settings) {
        return issueService.listAssignedIssues(settings)
                .collectList()
                .map(ResponseResult::success);
    }
}
