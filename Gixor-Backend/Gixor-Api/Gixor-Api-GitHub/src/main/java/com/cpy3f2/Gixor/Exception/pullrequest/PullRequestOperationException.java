package com.cpy3f2.Gixor.Exception.pullrequest;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.GitHubApiException;

public class PullRequestOperationException extends GitHubApiException {
    public PullRequestOperationException(String message, ResponseResult result) {
        super(message, result);
    }
} 