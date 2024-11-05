package com.cpy3f2.Gixor.Service;



import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.IssueComment;
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
 * @last : 2024-11-04 23:18
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 23:18
 */
@Service
public class IssueCommentService {

    @Resource
    private WebClient githubClient;

    /**
     * 获取仓库Issue的评论列表
     */
    public Flux<IssueComment> listComments(String owner, String repo, Integer issueNumber, BaseQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/repos/{owner}/{repo}/issues/{issue_number}/comments"),
                                settings)
                        .build(owner, repo, issueNumber))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取评论列表失败")))
                .bodyToFlux(IssueComment.class);
    }

    /**
     * 获取单个评论
     */
    public Mono<IssueComment> getComment(String owner, String repo, Long commentId) {
        return githubClient.get()
                .uri("/repos/{owner}/{repo}/issues/comments/{comment_id}",
                        owner, repo, commentId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取评论失败")))
                .bodyToMono(IssueComment.class);
    }

    /**
     * 创建评论
     */
    public Mono<IssueComment> createComment(String owner, String repo, Integer issueNumber, String body) {
        return githubClient.post()
                .uri("/repos/{owner}/{repo}/issues/{issue_number}/comments",
                        owner, repo, issueNumber)
                .bodyValue(new CommentRequest(body))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("创建评论失败")))
                .bodyToMono(IssueComment.class);
    }

    /**
     * 更新评论
     */
    public Mono<IssueComment> updateComment(String owner, String repo, Long commentId, String body) {
        return githubClient.patch()
                .uri("/repos/{owner}/{repo}/issues/comments/{comment_id}",
                        owner, repo, commentId)
                .bodyValue(new CommentRequest(body))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("更新评论失败")))
                .bodyToMono(IssueComment.class);
    }

    /**
     * 删除评论
     */
    public Mono<Void> deleteComment(String owner, String repo, Long commentId) {
        return githubClient.delete()
                .uri("/repos/{owner}/{repo}/issues/comments/{comment_id}",
                        owner, repo, commentId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("删除评论失败")))
                .bodyToMono(Void.class);
    }

    private record CommentRequest(String body) {}
}