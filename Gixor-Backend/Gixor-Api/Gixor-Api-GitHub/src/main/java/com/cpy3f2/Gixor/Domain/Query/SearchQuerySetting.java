package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 11:13
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 11:13
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuerySetting extends BaseQuerySetting{
    private String q;
}
