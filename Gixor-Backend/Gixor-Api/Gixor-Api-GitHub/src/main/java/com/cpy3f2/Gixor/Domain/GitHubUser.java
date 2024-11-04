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

    @Field(name = "total_Stars_Earned", type = FieldType.Long)
    private Integer totalStars;

    @Field(name = "total_Commits_2024", type = FieldType.Long)
    private Integer totalCommits;

    @Field(name = "total_PRs", type = FieldType.Long)
    private Integer totalPRs;

    @Field(name = "total_Issues", type = FieldType.Long)
    private Integer totalIssues;

    @Field(name = "contributed_to_last_year", type = FieldType.Long)
    private Integer contributedTo;

    @Field(name = "grade", type = FieldType.Keyword)
    private String grade;

    @Field(name = "score", type = FieldType.Double)
    private BigDecimal score;

    @Field(name = "total_PRs_Merged", type = FieldType.Long)
    private Long totalPRsMerged;

    @Field(name = "merged_PRs_Percentage", type = FieldType.Double)
    private Double mergedPRsPercentage;

    @Field(name = "total_PRs_Reviewed", type = FieldType.Long)
    private Long totalPRsReviewed;

    @Field(name = "total_Discussions_Started", type = FieldType.Long)
    private Long totalDiscussionsStarted;

    @Field(name = "total_Discussions_Answered", type = FieldType.Long)
    private Long totalDiscussionsAnswered;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
