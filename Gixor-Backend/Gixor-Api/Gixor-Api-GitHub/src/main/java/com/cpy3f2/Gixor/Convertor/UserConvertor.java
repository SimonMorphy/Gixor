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
        int totalStars = StreamSupport.stream(reposNode.spliterator(), false)
                .mapToInt(repo -> repo.path("stargazerCount").asInt())
                .sum();
        // 获取PR数据
        int totalPRs = userNode.path("pullRequests").path("totalCount").asInt();
        // 使用别名获取已合并PR数量
        int mergedPRs = userNode.path("mergedPullRequests").path("totalCount").asInt();
        double mergedPercentage = totalPRs > 0 ? (double) mergedPRs / totalPRs * 100 : 0;

        return GitHubUser.builder()
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
                .totalStars(totalStars)
                .totalCommits(contributionsNode.path("totalCommitContributions").asInt() +
                        contributionsNode.path("restrictedContributionsCount").asInt())
                .totalPRs( totalPRs)
                .totalIssues(userNode.path("issues").path("totalCount").asInt())
                .contributedTo(userNode.path("repositoriesContributedTo").path("totalCount").asInt())
                .totalPRsMerged((long) mergedPRs)
                .mergedPRsPercentage(mergedPercentage)
                .totalPRsReviewed((long) contributionsNode.path("totalPullRequestReviewContributions").asInt())
                .totalDiscussionsStarted((long) userNode.path("repositoryDiscussions").path("totalCount").asInt())
                .totalDiscussionsAnswered((long) userNode.path("repositoryDiscussionComments").path("totalCount").asInt())
                .createdAt(parseDateTime(userNode.path("createdAt")))
                .updatedAt(parseDateTime(userNode.path("updatedAt")))
                .build();
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


    private static int getIntOrZero(JsonNode node, String field) {
        return node.path(field).asInt(0);
    }
}
