package com.cpy3f2.Gixor.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *  GitHub Event
 * @author simon
 * @since 2024/11/5 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String id;
    private String type;
    private Actor actor;
    private Repo repo;
    private Payload payload;
    private Boolean isPublic;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Actor {
        private Long id;
        private String login;
        @JsonProperty("display_login")
        private String displayLogin;
        @JsonProperty("gravatar_id")
        private String gravatarId;
        private String url;
        @JsonProperty("avatar_url")
        private String avatarUrl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repo {
        private Long id;
        private String name;
        private String url;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        private String action;
        @JsonProperty("push_id")
        private Long pushId;
        private Integer size;
        @JsonProperty("distinct_size") 
        private Integer distinctSize;
        private String ref;
        private String head;
        private String before;
        private String description;
        private String masterBranch;
    }
} 