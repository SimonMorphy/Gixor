package com.cpy3f2.Gixor.Domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 19:28
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 19:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForkDTO {
    private String organization;
    private String name;
    @JsonProperty("default_branch_only")
    private Boolean defaultBranchOnly;
}
