package com.cpy3f2.Gixor.Domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description : 评论
 * @last : 2024-11-04 23:16
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 23:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueComment {
    private Long id;

    @JsonProperty("node_id")
    private String nodeId;

    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String body;

    private GitHubUser user;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("issue_url")
    private String issueUrl;

    @JsonProperty("author_association")
    private String authorAssociation;
}