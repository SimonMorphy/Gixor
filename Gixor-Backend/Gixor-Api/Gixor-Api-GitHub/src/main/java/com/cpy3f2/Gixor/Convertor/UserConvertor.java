package com.cpy3f2.Gixor.Convertor;

import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.StreamSupport;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 02:48
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 02:48
 */
public class UserConvertor {
    public static GitHubUser convertToGitHubUser(JsonNode userNode) {
        JsonNode contributionsNode = userNode.path("contributionsCollection");
        JsonNode reposNode = userNode.path("repositories").path("nodes");
        
        // 计算总star数
        long totalStars = StreamSupport.stream(reposNode.spliterator(), false)
                .mapToLong(repo -> repo.path("stargazerCount").asLong())
                .sum();
                
        // 获取PR数据
        long totalPRs = userNode.path("pullRequests").path("totalCount").asLong();
        long mergedPRs = userNode.path("mergedPullRequests").path("totalCount").asLong();
        float mergedPercentage = totalPRs > 0 
            ? (float)((double) mergedPRs / totalPRs * 100)
            : 0f;

        // 构建基础用户信息
        GitHubUser.GitHubUserBuilder builder = GitHubUser.builder()
                .githubId(getTextOrEmpty(userNode, "id"))
                .login(getTextOrEmpty(userNode, "login"))
                .name(getTextOrEmpty(userNode, "name"))
                .avatarUrl(getTextOrEmpty(userNode, "avatarUrl"))
                .htmlUrl(getTextOrEmpty(userNode, "url"))
                .company(getTextOrEmpty(userNode, "company"))
                .blog(getTextOrEmpty(userNode, "websiteUrl"))
                .location(getTextOrEmpty(userNode, "location"))
                .email(getTextOrEmpty(userNode, "email"))
                .bio(getTextOrEmpty(userNode, "bio"))
                .publicRepos(userNode.path("repositories").path("totalCount").asInt())
                .followers(userNode.path("followers").path("totalCount").asInt())
                .following(userNode.path("following").path("totalCount").asInt())
                .watchRepos(userNode.path("watching").path("totalCount").asInt());

        // 构建ES统计数据
        builder.totalStars(totalStars)
                .totalCommits(getLongValue(contributionsNode, "totalCommitContributions"))
                .totalPRs(totalPRs)
                .totalPRsMerged(mergedPRs)
                .mergedPRsPercentage(mergedPercentage)
                .totalPRsReviewed(getLongValue(contributionsNode, "totalPullRequestReviewContributions"))
                .totalIssues(getLongValue(userNode.path("issues"), "totalCount"))
                .totalDiscussionsStarted(getLongValue(userNode.path("repositoryDiscussions"), "totalCount"))
                .totalDiscussionsAnswered(getLongValue(userNode.path("repositoryDiscussionComments"), "totalCount"))
                .contributedTo(getLongValue(userNode.path("repositoriesContributedTo"), "totalCount"));

        // 时间字段
        builder.createdAt(parseDateTime(userNode.path("createdAt")))
                .updatedAt(parseDateTime(userNode.path("updatedAt")));

        // 默认值
        builder.grade("") // 等级需要单独计算
                .score(0f) // 分数需要单独计算
                .majorDomains(null) // 主要领域需要单独分析
                .domainWeights(null) // 领域权重需要单独分析
                .nation(""); // 国家/地区需要单独设置

        return builder.build();
    }

    private static String getTextOrEmpty(JsonNode node, String field) {
        JsonNode fieldNode = node.path(field);
        return fieldNode.isMissingNode() || fieldNode.isNull() ? "" : fieldNode.asText();
    }

    private static LocalDateTime parseDateTime(JsonNode node) {
        String dateStr = node.asText("");
        if (dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static Long getLongValue(JsonNode node, String field) {
        JsonNode fieldNode = node.path(field);
        return fieldNode.isMissingNode() || fieldNode.isNull() ? 0L : fieldNode.asLong();
    }

    // 添加一个辅助方法来处理百分比字符串
    private static Float parsePercentage(String percentStr) {
        if (percentStr == null || percentStr.isEmpty()) {
            return 0f;
        }
        try {
            return Float.parseFloat(percentStr.replace("%", "").trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}
