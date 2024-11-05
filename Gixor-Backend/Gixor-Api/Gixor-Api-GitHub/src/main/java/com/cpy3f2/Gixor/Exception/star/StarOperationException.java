package com.cpy3f2.Gixor.Exception.star;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.GitHubApiException;

public class StarOperationException extends GitHubApiException {
    public StarOperationException(String message, ResponseResult result) {
        super(message, result);
    }
} 