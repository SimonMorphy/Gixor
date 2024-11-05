package com.cpy3f2.Gixor.Exception;

import com.cpy3f2.Gixor.Domain.ResponseResult;

public class GitHubApiException extends RuntimeException {
    private final ResponseResult result;

    public GitHubApiException(String message, ResponseResult result) {
        super(message);
        this.result = result;
    }

    public ResponseResult getResult() {
        return result;
    }
} 