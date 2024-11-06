package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-07 04:15
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-07 04:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "github_stats")
public class StatisticUser {
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
    @Field(name = "total_stars_earned", type = FieldType.Long)
    private Long totalStars;

    @Field(name = "total_commits_2024", type = FieldType.Long)
    private Long totalCommits;

    @Field(name = "total_prs", type = FieldType.Long)
    private Long totalPRs;

    @Field(name = "total_prs_merged", type = FieldType.Long)
    private Long totalPRsMerged;

    @Field(name = "merged_prs_percentage", type = FieldType.Float)
    private Float mergedPRsPercentage;

    @Field(name = "total_prs_reviewed", type = FieldType.Long)
    private Long totalPRsReviewed;

    @Field(name = "total_issues", type = FieldType.Text)
    private Long totalIssues;

    @Field(name = "total_discussions_started", type = FieldType.Text)
    private Long totalDiscussionsStarted;

    @Field(name = "total_discussions_answered", type = FieldType.Text)
    private Long totalDiscussionsAnswered;

    @Field(name = "contributed_to_last_year", type = FieldType.Text)
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
}
