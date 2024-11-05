package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 09:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 09:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUser {
    @JsonProperty("id")
    private String githubId;
    @JsonProperty("login")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
}
