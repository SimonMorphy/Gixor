package com.cpy3f2.Gixor.Domain.VO;

import com.cpy3f2.Gixor.Domain.GitHubRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 16:50
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 16:50
 */
@Data
public class RepositorySearchVO {
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("incomplete_results")
    private Boolean incompleteResults;

    @JsonProperty("items")
    private List<GitHubRepository> repositories;
}
