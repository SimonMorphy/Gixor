package com.cpy3f2.Gixor.Exception.notification;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.GitHubApiException;

public class NotificationOperationException extends GitHubApiException {
    public NotificationOperationException(String message, ResponseResult result) {
        super(message, result);
    }
} 