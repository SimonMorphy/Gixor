package com.cpy3f2.Gixor.Domain;
/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 22:57
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 22:57
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequest {
    private Long id;
    private Integer number;
    private String state;
    private String title;
    private String body;
    private Boolean locked;

    @JsonProperty("active_lock_reason")
    private String activeLockReason;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("closed_at")
    private LocalDateTime closedAt;

    @JsonProperty("merged_at")
    private LocalDateTime mergedAt;

    @JsonProperty("merge_commit_sha")
    private String mergeCommitSha;

    @JsonProperty("draft")
    private Boolean isDraft;

    private GitHubUser user;
    private List<Issue.Label> labels;
    private Issue.Milestone milestone;
    private GitHubUser assignee;
    private List<GitHubUser> assignees;

    @JsonProperty("requested_reviewers")
    private List<GitHubUser> requestedReviewers;

    @JsonProperty("requested_teams")
    private List<Team> requestedTeams;

    private PullRequestRef head;
    private PullRequestRef base;

    @JsonProperty("author_association")
    private String authorAssociation;

    @JsonProperty("auto_merge")
    private Boolean autoMerge;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        private Long id;

        @JsonProperty("node_id")
        private String nodeId;

        private String name;
        private String slug;
        private String description;
        private String privacy;
        private String permission;

        @JsonProperty("notification_setting")
        private String notificationSetting;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PullRequestRef {
        private String label;
        private String ref;
        private String sha;
        private GitHubUser user;
        private GitHubRepository repo;
    }
}