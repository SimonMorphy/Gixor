package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 22:02
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 22:02
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionQuerySetting extends BaseQuerySetting {
    private Integer first;
    private String category;
    private String after;
    private String orderBy;
    private String direction;
}