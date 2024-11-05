package com.cpy3f2.Gixor.Exception.user;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.GitHubApiException;

/**
 *  用户操作异常
 * @author simon
 * @since 2024/11/5 */

public class UserOperationException extends GitHubApiException {
    public UserOperationException(String message, ResponseResult result) {
        super(message, result);
    }
} 