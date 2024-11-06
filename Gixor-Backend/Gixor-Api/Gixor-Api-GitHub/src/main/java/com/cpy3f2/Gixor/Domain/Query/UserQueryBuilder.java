package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 02:03
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 02:03
 */
public class UserQueryBuilder {
    private static final String USER_QUERY_TEMPLATE = """
        query {
          user(login: "%s") {
            id
            login
            name
            avatarUrl
            url
            company
            websiteUrl
            location
            email
            bio
            repositories(privacy: PUBLIC, ownerAffiliations: [OWNER], first: 100) {
              nodes {
                stargazerCount
              }
              totalCount
            }
            watching {
              totalCount
            }
            repositoriesContributedTo(contributionTypes: [COMMIT, ISSUE, PULL_REQUEST, REPOSITORY]) {
              totalCount
            }
            pullRequests(first: 1) {
              totalCount
            }
            mergedPullRequests: pullRequests(states: [MERGED], first: 1) {
              totalCount
            }
            issues {
              totalCount
            }
            contributionsCollection {
              totalCommitContributions
              restrictedContributionsCount
              totalIssueContributions
              totalPullRequestContributions
              totalPullRequestReviewContributions
            }
            followers {
              totalCount
            }
            following {
              totalCount
            }
            starredRepositories {
              totalCount
            }
            repositoryDiscussions {
              totalCount
            }
            repositoryDiscussionComments {
              totalCount
            }
            createdAt
            updatedAt
          }
        }
    """;

    public static String buildUserQuery(String username) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String query = String.format(USER_QUERY_TEMPLATE, username);
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("query", query);
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("查询失败", e);
        }
    }


}
