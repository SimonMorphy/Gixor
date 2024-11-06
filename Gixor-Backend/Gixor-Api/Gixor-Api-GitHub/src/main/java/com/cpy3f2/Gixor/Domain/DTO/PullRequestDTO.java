package com.cpy3f2.Gixor.Domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 09:36
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 09:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDTO {
    private String title;           // PR标题
    private String head;            // 源分支
    private String base;            // 目标分支
    private String body;            // PR描述
    
    @JsonProperty("draft")
    private Boolean isDraft;        // 是否为草稿PR
    
    @JsonProperty("maintainer_can_modify")
    private Boolean maintainerCanModify;
    
    // 可选：如果需要指定仓库，可以添加
    @JsonProperty("head_repo")
    private String headRepo;    
}
