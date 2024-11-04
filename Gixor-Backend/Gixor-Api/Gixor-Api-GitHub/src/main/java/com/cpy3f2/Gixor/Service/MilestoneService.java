package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.Issue.Milestone;
import com.cpy3f2.Gixor.Domain.Query.IssueQuerySetting;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description : GitHub Milestone相关服务
 * @last : 2024-11-04 02:22
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 02:22
 */
@Service
public class MilestoneService {
    
    @Resource
    private WebClient githubClient;

    /**
     * 获取仓库的里程碑列表
     */
    public Flux<Milestone> listMilestones(String owner, String repo, IssueQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/repos/{owner}/{repo}/milestones"),
                        settings)
                        .build(owner, repo))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取里程碑列表失败")))
                .bodyToFlux(Milestone.class);
    }

    /**
     * 获取单个里程碑
     */
    public Mono<Milestone> getMilestone(String owner, String repo, Integer milestoneNumber) {
        return githubClient.get()
                .uri("/repos/{owner}/{repo}/milestones/{milestone_number}", 
                     owner, repo, milestoneNumber)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取里程碑详情失败")))
                .bodyToMono(Milestone.class);
    }

    /**
     * 创建里程碑
     */
    public Mono<Milestone> createMilestone(String owner, String repo, Milestone milestone) {
        return githubClient.post()
                .uri("/repos/{owner}/{repo}/milestones", owner, repo)
                .bodyValue(milestone)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("创建里程碑失败")))
                .bodyToMono(Milestone.class);
    }

    /**
     * 更新里程碑
     */
    public Mono<Milestone> updateMilestone(String owner, String repo, 
                                         Integer milestoneNumber, Milestone milestone) {
        return githubClient.patch()
                .uri("/repos/{owner}/{repo}/milestones/{milestone_number}", 
                     owner, repo, milestoneNumber)
                .bodyValue(milestone)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("更新里程碑失败")))
                .bodyToMono(Milestone.class);
    }

    /**
     * 删除里程碑
     */
    public Mono<Void> deleteMilestone(String owner, String repo, Integer milestoneNumber) {
        return githubClient.delete()
                .uri("/repos/{owner}/{repo}/milestones/{milestone_number}", 
                     owner, repo, milestoneNumber)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("删除里程碑失败")))
                .bodyToMono(Void.class);
    }
}
