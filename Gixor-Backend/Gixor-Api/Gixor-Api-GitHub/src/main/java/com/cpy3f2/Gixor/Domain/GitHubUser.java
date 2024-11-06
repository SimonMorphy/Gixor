package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @MultiField(
        mainField = @Field(name = "login", type = FieldType.Text, analyzer = "name_analyzer"),
        otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword)
        }
    )
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
    private Integer watchRepos;
    private Boolean followedByCurrentUser;
    private Boolean isCurrentUser;

    @Field(name = "Total_Stars_Earned", type = FieldType.Text)
    private Long totalStars;

    @Field(name = "Total_Commits_2024", type = FieldType.Text)
    private Long totalCommits;

    @Field(name = "Total_PRs", type = FieldType.Text)
    private Long totalPRs;

    @Field(name = "Total_PRs_Merged", type = FieldType.Text)
    private Long totalPRsMerged;

    @Field(name = "Merged_PRs_Percentage", type = FieldType.Text)
    private Float mergedPRsPercentage;

    @Field(name = "Total_PRs_Reviewed", type = FieldType.Text)
    private Long totalPRsReviewed;

    @Field(name = "Total_Issues", type = FieldType.Text)
    private Long totalIssues;

    @Field(name = "Total_Discussions_Started", type = FieldType.Text)
    private Long totalDiscussionsStarted;

    @Field(name = "Total_Discussions_Answered", type = FieldType.Text)
    private Long totalDiscussionsAnswered;

    @Field(name = "Contributed_to_last_year", type = FieldType.Text)
    private Long contributedTo;

    @Field(name = "grade", type = FieldType.Keyword)
    private String grade;

    @Field(name = "score", type = FieldType.Float)
    private Float score;

    @Field(name = "major_domains", type = FieldType.Keyword)
    private List<String> majorDomains;

    @Field(name = "domain_weights", type = FieldType.Object)
    private Map<String, Object> domainWeights;

    @MultiField(
        mainField = @Field(name = "nation", type = FieldType.Text, analyzer = "name_analyzer"),
        otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword)
        }
    )
    private String nation;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
