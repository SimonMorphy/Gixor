package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 21:47
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 21:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discussion {
    private String title;
    private String body;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("author")
    private String authorName;


    @JsonProperty("category")
    private String categoryName;


    private List<Comment> comments;

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Comment {
        private String body;
        private String authorName;
    }
}