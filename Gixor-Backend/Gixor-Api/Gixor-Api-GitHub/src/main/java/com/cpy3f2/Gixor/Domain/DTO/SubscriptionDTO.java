package com.cpy3f2.Gixor.Domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  SubscriptionDTO
 * @author simon
 * @since 2024/11/4 */
@Data
public class SubscriptionDTO {
    private Boolean subscribed;
    private Boolean ignored;
    private String reason;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    private String url;
    
    @JsonProperty("repository_url")
    private String repositoryUrl;
} 