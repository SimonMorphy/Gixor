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
public class RepositoryQuerySetting extends BaseQuerySetting {
    private String visibility;
    private String affiliation;
    private String type;
    private String language;
    private Boolean archived;
    private Boolean fork;
}
