package com.cpy3f2.Gixor.Domain;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.HashMap;
import java.util.Objects;

/**
 * @description : 返回结果
 * @author : simon
 * @since : 2024-10-24 20:06
 * @last : 2024-10-24 20:06
 * Copyright (c) 2024. 保留所有权利。
 */
public class ResponseResult extends HashMap<String, Object> {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    public ResponseResult(Integer code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    public static ResponseResult success() {
        return ResponseResult.success("操作成功!");
    }

    public static ResponseResult success(Object data) {
        return ResponseResult.success("操作成功!", data);
    }

    public static ResponseResult success(String msg) {
        return new ResponseResult(HttpStatus.OK.value(), msg);
    }

    public static ResponseResult success(String msg, Object data) {
        return new ResponseResult(HttpStatus.OK.value(), msg, data);
    }

    public static ResponseResult error() {
        return ResponseResult.error("操作失败!");
    }

    public static ResponseResult error(String msg) {
        return ResponseResult.error(msg, null);
    }

    public static ResponseResult error(Integer code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    public static ResponseResult error(String msg, Object data) {
        return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static ResponseResult warn(String msg) {
        return warn(msg, null);
    }

    public static ResponseResult warn(String msg, Object data) {
        return new ResponseResult(HttpStatus.BAD_REQUEST.value(), msg, data);
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.OK.value(), this.get(CODE_TAG));
    }

    /**
     * 是否为警告消息
     *
     * @return 结果
     */
    public boolean isWarn() {
        return Objects.equals(HttpStatus.BAD_REQUEST.value(), this.get(CODE_TAG));
    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return Objects.equals(HttpStatus.INTERNAL_SERVER_ERROR.value(), this.get(CODE_TAG));
    }

    @Override
    public ResponseResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
