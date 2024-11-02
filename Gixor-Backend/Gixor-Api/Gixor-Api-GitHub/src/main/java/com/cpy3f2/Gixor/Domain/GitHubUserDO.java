package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-01 00:37
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-01 00:37
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserDO {
    private String name;
    private String email;
    private String blog;
    private String bio;
    private String company;
    private String location;
    @JsonProperty("twitter_username")
    private String twitterUsername;
    private Boolean hireable;
}
