package com.cpy3f2.Gixor.Config;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-28 17:18
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-28 17:18
 */


public class ReactiveReplayDecoder implements Decoder {
    private final Decoder delegate;

    public ReactiveReplayDecoder(Decoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(Mono.class)) {
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
            Object decoded = delegate.decode(response, actualType);
            if (decoded instanceof ResponseResult) {
                return Mono.just(decoded);
            }
            return Mono.just(ResponseResult.success(decoded));
        }
        return delegate.decode(response, type);
    }
}
