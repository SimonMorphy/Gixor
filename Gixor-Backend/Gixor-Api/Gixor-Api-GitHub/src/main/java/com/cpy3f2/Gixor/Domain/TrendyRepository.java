package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 17:31
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 17:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendyRepository {
    private String author;
    private String name;
    private String avatar;
    private String url;
    private String description;
    private String language;
    private String languageColor;
    private Integer stars;
    private Integer forks;
    private Integer currentPeriodStars;
    private List<Contributor> builtBy;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contributor {
        private String username;
        private String href;
        private String avatar;
    }
}

