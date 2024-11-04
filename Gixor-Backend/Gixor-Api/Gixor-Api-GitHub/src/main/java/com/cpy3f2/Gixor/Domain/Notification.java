package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 *
 * @author simon
 * @since 2024/11/4 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    private String id;
    private Repository repository;
    private Subject subject;
    private String reason;
    private boolean unread;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    @JsonProperty("last_read_at")
    private LocalDateTime lastReadAt;
    
    private String url;
    
    @JsonProperty("subscription_url")
    private String subscriptionUrl;

    @Data
    public static class Repository {
        private Long id;
        
        @JsonProperty("node_id")
        private String nodeId;
        
        private String name;
        
        @JsonProperty("full_name")
        private String fullName;
        
        private Owner owner;
        private boolean isPrivate;
        
        @JsonProperty("html_url")
        private String htmlUrl;
        
        private String description;
    }

    @Data
    public static class Owner {
        private String login;
        private Long id;
        
        @JsonProperty("node_id")
        private String nodeId;
        
        @JsonProperty("avatar_url")
        private String avatarUrl;
        
        @JsonProperty("html_url")
        private String htmlUrl;
        
        private String type;
        
        @JsonProperty("site_admin")
        private boolean siteAdmin;
    }

    @Data
    public static class Subject {
        private String title;
        private String url;
        
        @JsonProperty("latest_comment_url")
        private String latestCommentUrl;
        
        private String type;
    }
}