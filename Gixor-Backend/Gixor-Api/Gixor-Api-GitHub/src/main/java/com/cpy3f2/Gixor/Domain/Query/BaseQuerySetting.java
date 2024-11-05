package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 11:11
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 11:11
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuerySetting {
    @JsonProperty("per_page")
    private Integer perPage;
    private Integer page;
    private String sort;
    private String direction;
}
