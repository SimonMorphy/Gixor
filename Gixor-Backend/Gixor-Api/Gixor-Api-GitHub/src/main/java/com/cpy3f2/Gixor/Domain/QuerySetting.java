package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-31 16:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-31 16:23
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuerySetting {

    @JsonProperty("per_page")
    private Integer perPage;
    private Integer page;

    private String sort;
    private String direction;

    private Boolean all;
    private Boolean participating;
    private LocalDateTime since;
    private LocalDateTime before;

    private String state;
    private String visibility;
    private String affiliation;
    private String type;
    private String language;
    private String q;
    private Boolean archived;
    private Boolean fork;
}
