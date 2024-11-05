package com.cpy3f2.Gixor.Exception.repository;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Exception.GitHubApiException;

/**
 * Repository
 * @author simon
 * @since 2024/11/5 */
public class RepositoryOperationException extends GitHubApiException {
    public RepositoryOperationException(String message, ResponseResult result) {
        super(message, result);
    }
} 