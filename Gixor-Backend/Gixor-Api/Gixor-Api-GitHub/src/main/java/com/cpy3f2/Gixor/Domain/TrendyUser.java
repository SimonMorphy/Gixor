package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 11:01
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendyUser {
    private String username;
    private String displayName;
    private String repoName;
    private String avatarUrl;
    private String description;
}
