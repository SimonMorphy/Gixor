package com.cpy3f2.Gixor.Domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 00:48
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 00:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDTO {
    private String title;
    private String body;
    private List<String> assignees;

    private Long milestone;

    private List<String> labels;
    private String state;
    @JsonProperty("state_reason")
    private String stateReason;
}
