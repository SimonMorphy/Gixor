package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-31 16:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-31 16:23
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuerySetting {
    private String sort;
    private String direction;
    private Integer perPage;
    private Integer page;
}
