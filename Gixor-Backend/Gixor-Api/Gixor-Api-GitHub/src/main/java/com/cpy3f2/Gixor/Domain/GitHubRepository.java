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
 * @description : GitHub仓库信息实体类
 * @since : 2024-10-30 19:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepository {
    @JsonProperty("id")
    private Long githubId;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private Owner owner;
    private License license;
    private String description;
    @JsonProperty("open_issues")
    private Integer issues;
    private String language;
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;
    @JsonProperty("forks_count")
    private Integer forksCount;
    private String visibility;
    private Boolean fork;
    @JsonProperty("html_url")
    private String htmlUrl;
    private List<String> topics;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("pushed_at")
    private LocalDateTime pushedAt;
    private Integer size;
    @JsonProperty("default_branch")
    private String defaultBranch;

    /**
     * @author : simon
     * @description : GitHub仓库所有者信息
     * @since : 2024-10-30 19:43
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {
        @JsonProperty("id")
        private Long githubId;
        @JsonProperty("login")
        private String name;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        @JsonProperty("html_url")
        private String htmlUrl;
        private String type;
    }

    /**
     * 仓库的许可协议信息
     * @author simon
     * @since 2024/10/31     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class License {
        private String name;
        @JsonProperty("spdx_id")
        private String spdId;
        private String key;
        private String url;
    }
}