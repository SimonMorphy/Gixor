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
    private String title;
    private String head;
    private String base;
    private String body;
    private Boolean draft;
    @JsonProperty("maintainer_can_modify")
    private Boolean maintainerCanModify;
}
