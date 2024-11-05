package com.cpy3f2.Gixor.Domain.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  SubscriptionVO
 * @author simon
 * @since 2024/11/4 */
@Data
public class SubscriptionVO {
    private Boolean subscribed;
    private Boolean ignored;
    private String reason;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    private String url;
    
    @JsonProperty("repository_url")
    private String repositoryUrl;
} 