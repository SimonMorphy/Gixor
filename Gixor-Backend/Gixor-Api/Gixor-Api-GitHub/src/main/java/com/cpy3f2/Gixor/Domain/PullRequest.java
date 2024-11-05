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
 * @last : 2024-11-04 22:57
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 22:57
 */
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

    @JsonProperty("html_url")
    private String htmlUrl;

    private SimpleUser user;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("closed_at")
    private LocalDateTime closedAt;

    @JsonProperty("merged_at")
    private LocalDateTime mergedAt;

    @JsonProperty("draft")
    private Boolean isDraft;

    private PullRequestRef head;
    private PullRequestRef base;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PullRequestRef {
        private String label;
        private String ref;
        private String sha;
    }
}