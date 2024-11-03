package com.cpy3f2.Gixor.Domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 01:42
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 01:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Watcher {
    private String login;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String type;

    @JsonProperty("site_admin")
    private Boolean siteAdmin;
}
