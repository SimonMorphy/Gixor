package com.cpy3f2.Gixor.Domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 23:03
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 23:03
 */
@Data
@Builder
public class Thread {
    private String id;
    private Boolean unread;
    private String url;
    private String subscriptionUrl;
    private ThreadSubscription subscription;
    @Data
    @Builder
    public static class ThreadSubscription {
        private Boolean subscribed;
        private Boolean ignored;
        private String reason;
        private String createdAt;
        private String url;
        private String threadUrl;
    }
}
