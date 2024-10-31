package com.cpy3f2.Gixor.Handler;

import cn.hutool.core.convert.Convert;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.html.EscapeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author simon
 * @since 2024/10/24 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseResult> handleRuntimeException(RuntimeException e, ServerWebExchange exchange)
    {
        log.error("请求地址'{}',发生未知异常.", exchange.getRequest().getURI().getPath(), e);
        return Mono.just(ResponseResult.error(e.getMessage()));
    }


    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Mono<ResponseResult> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, ServerWebExchange exchange)
    {
        String requestURI = exchange.getRequest().getURI().getPath();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return Mono.just(ResponseResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value)));
    }

}
