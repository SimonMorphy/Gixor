package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 19:43
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 19:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "github_stats")
public class GitHubUser {
    @Id
    @Field(name = "id", type = FieldType.Keyword)
    @JsonProperty("id")
    private String githubId;

    @Field(name = "name", type = FieldType.Text)
    private String login;

    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")
    private String htmlUrl;

    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;

    @JsonProperty("public_repos")
    private Integer publicRepos;
    private Integer followers;
    private Integer following;

    private Boolean followedByCurrentUser;
    private Boolean isCurrentUser;

    @Field(name = "Total_Stars_Earned", type = FieldType.Integer)
    private Integer totalStars;

    @Field(name = "Total_Commits_2024", type = FieldType.Integer)
    private Integer totalCommits;

    @Field(name = "Total_PRs", type = FieldType.Integer)
    private Integer totalPRs;

    @Field(name = "Total_Issues", type = FieldType.Integer)
    private Integer totalIssues;

    @Field(name = "Contributed_to_last_year", type = FieldType.Integer)
    private Integer contributedTo;

    @Field(name = "grade", type = FieldType.Keyword)
    private String grade;

    @Field(name = "score", type = FieldType.Double)
    private BigDecimal score;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
