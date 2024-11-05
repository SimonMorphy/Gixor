package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 11:12
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 11:12
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class IssueQuerySetting extends BaseQuerySetting {
    private String state;
    private String filter;
    private String labels;
    private Boolean collab;
    private Boolean orgs;
    private Boolean owned;
    private Boolean pulls;
}