package com.cpy3f2.Gixor.Domain.Query;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 11:21
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 11:21
 */

@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneQuerySetting extends IssueQuerySetting {
    @JsonProperty("sort")
    private String sortBy;
    private String description;
}
