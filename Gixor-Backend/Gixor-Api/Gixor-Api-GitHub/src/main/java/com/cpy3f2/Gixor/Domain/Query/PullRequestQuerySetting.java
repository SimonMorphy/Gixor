package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Pull Request查询设置
 * @author simon
 * @since 2024/11/5
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestQuerySetting extends BaseQuerySetting {
    private String state;
    private String head;
    private String base;
    private String sort;
    private String direction;
    
    @JsonProperty("draft")
    private Boolean isDraft;
    
    private String labels;
    private String milestone;
}
