package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.DTO.IssueDTO;
import com.cpy3f2.Gixor.Domain.Issue.Milestone;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.Query.IssueQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.IssueCommentService;
import com.cpy3f2.Gixor.Service.IssueService;
import com.cpy3f2.Gixor.Service.MilestoneService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
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

    @Resource
    private MilestoneService milestoneService;

    @Resource
    private IssueCommentService issueCommentService;

    /**
     * 获取Issue列表
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetMapping
    public Mono<ResponseResult> listIssues(IssueQuerySetting settings) {
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
    @GetMapping("/{owner}/{repo}")
    public Mono<ResponseResult> listRepoIssues(@PathVariable String owner, 
                                             @PathVariable String repo,
                                             IssueQuerySetting settings) {
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
    @PostMapping("/{owner}/{repo}")
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
    @GetMapping("/{owner}/{repo}/{number}")
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
    @PatchMapping("/{owner}/{repo}/{number}")
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
    @DeleteMapping("/{owner}/{repo}/{issueNumber}")
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
    @GetMapping("/assigned")
    public Mono<ResponseResult> listAssignedIssues(IssueQuerySetting settings) {
        return issueService.listAssignedIssues(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取仓库的里程碑列表
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetMapping("/{owner}/{repo}/milestones")
    public Mono<ResponseResult> listMilestones(@PathVariable String owner,
                                             @PathVariable String repo,
                                               IssueQuerySetting settings) {
        return milestoneService.listMilestones(owner, repo, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取单个里程碑详情
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param milestoneNumber 里程碑编号
     * @return Mono<ResponseResult>
     */
    @GetMapping("/{owner}/{repo}/milestones/{milestone_number}")
    public Mono<ResponseResult> getMilestone(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @PathVariable("milestone_number") Integer milestoneNumber) {
        return milestoneService.getMilestone(owner, repo, milestoneNumber)
                .map(ResponseResult::success);
    }

    /**
     * 创建里程碑
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param milestone 里程碑信息
     * @return Mono<ResponseResult>
     */
    @PostMapping("/{owner}/{repo}/milestones")
    public Mono<ResponseResult> createMilestone(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @RequestBody Milestone milestone) {
        return milestoneService.createMilestone(owner, repo, milestone)
                .map(m -> ResponseResult.success("创建成功"));
    }

    /**
     * 更新里程碑
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param milestoneNumber 里程碑编号
     * @param milestone 里程碑更新信息
     * @return Mono<ResponseResult>
     */
    @PatchMapping("/{owner}/{repo}/milestones/{milestone_number}")
    public Mono<ResponseResult> updateMilestone(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @PathVariable("milestone_number") Integer milestoneNumber,
                                              @RequestBody Milestone milestone) {
        return milestoneService.updateMilestone(owner, repo, milestoneNumber, milestone)
                .map(m -> ResponseResult.success("更新成功"));
    }

    /**
     * 删除里程碑
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param milestoneNumber 里程碑编号
     * @return Mono<ResponseResult>
     */
    @DeleteMapping("/{owner}/{repo}/milestones/{milestone_number}")
    public Mono<ResponseResult> deleteMilestone(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @PathVariable("milestone_number") Integer milestoneNumber) {
        return milestoneService.deleteMilestone(owner, repo, milestoneNumber)
                .then(Mono.just(ResponseResult.success("删除成功")));
    }

    /**
     * 获取Issue评论列表
     */
    @GetMapping("/{owner}/{repo}/issues/{issue_number}/comments")
    public Mono<ResponseResult> listComments(@PathVariable String owner,
                                             @PathVariable String repo,
                                             @PathVariable("issue_number") Integer issueNumber,
                                             BaseQuerySetting settings) {
        return issueCommentService.listComments(owner, repo, issueNumber, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取单个评论
     */
    @GetMapping("/{owner}/{repo}/comments/{comment_id}")
    public Mono<ResponseResult> getComment(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @PathVariable("comment_id") Long commentId) {
        return issueCommentService.getComment(owner, repo, commentId)
                .map(ResponseResult::success);
    }

    /**
     * 创建评论
     */
    @PostMapping("/{owner}/{repo}/issues/{issue_number}/comments")
    public Mono<ResponseResult> createComment(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @PathVariable("issue_number") Integer issueNumber,
                                              @RequestBody String body) {
        return issueCommentService.createComment(owner, repo, issueNumber, body)
                .map(comment -> ResponseResult.success("评论创建成功"));
    }

    /**
     * 更新评论
     */
    @PatchMapping("/{owner}/{repo}/comments/{comment_id}")
    public Mono<ResponseResult> updateComment(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @PathVariable("comment_id") Long commentId,
                                              @RequestBody String body) {
        return issueCommentService.updateComment(owner, repo, commentId, body)
                .map(comment -> ResponseResult.success("评论更新成功"));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{owner}/{repo}/comments/{comment_id}")
    public Mono<ResponseResult> deleteComment(@PathVariable String owner,
                                              @PathVariable String repo,
                                              @PathVariable("comment_id") Long commentId) {
        return issueCommentService.deleteComment(owner, repo, commentId)
                .then(Mono.just(ResponseResult.success("评论删除成功")));
    }
}
