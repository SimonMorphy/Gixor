package com.cpy3f2.Gixor.Exception.get;

import com.cpy3f2.Gixor.Domain.ResponseResult;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 00:42
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 00:42
 */
public class GetFailedException extends RuntimeException{
    private final ResponseResult result;

    public GetFailedException(ResponseResult result) {
        this.result = result;
    }

    public ResponseResult getResult() {
        return result;
    }
}
