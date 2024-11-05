package com.cpy3f2.Gixor.Domain.Query;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 11:19
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 11:19
 */


@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQuerySetting extends ActivityQuerySetting {
    private Boolean all;

    @JsonProperty("last_read_at")
    private String lastReadAt;

    private String reason;

    @JsonProperty("unread")
    private Boolean isUnread;

    @JsonProperty("before")
    private String beforeTime;

    private String subject;

    @JsonProperty("repository_full_name")
    private String repositoryFullName;
}