package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 00:35
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 00:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {
    private Long id;
    private Integer number;
    private String state;
    private String title;
    private String body;
    private GitHubUser user;
    private List<Label> labels;
    private GitHubUser assignee;
    private List<GitHubUser> assignees;
    private Milestone milestone;
    private Boolean locked;
    @JsonProperty("active_lock_reason")
    private String activeLockReason;
    private Integer comments;
    @JsonProperty("closed_at")
    private LocalDateTime closedAt;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    private GitHubRepository repository;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Label {
        private Long id;
        private String name;
        private String description;
        private String color;
        private Boolean defaultLabel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Milestone {
        private Long id;
        private Integer number;
        private String state;
        private String title;
        private String description;
        private GitHubUser creator;
        @JsonProperty("open_issues")
        private Integer openIssues;
        @JsonProperty("closed_issues")
        private Integer closedIssues;
        @JsonProperty("created_at")
        private LocalDateTime createdAt;
        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;
        @JsonProperty("closed_at")
        private LocalDateTime closedAt;
        @JsonProperty("due_on")
        private LocalDateTime dueOn;
    }
}
