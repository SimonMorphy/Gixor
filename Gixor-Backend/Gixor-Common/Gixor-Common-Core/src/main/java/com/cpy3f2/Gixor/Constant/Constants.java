package com.cpy3f2.Gixor.Constant;

import org.springframework.http.HttpStatus;

import java.util.function.Predicate;

/**
 *
 * @author simon
 * @since 2024/10/24 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";


    /**
     * www主域
     */
    public static final String WWW = "www.";
    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    public static final String ANDROID = "android";
    public static final Boolean TRUE = true;
    public static final long CACHE_EXPIRATION = 60*60*24*7;
    public static final String TOKEN = "token" ;
    public static final String ADMIN = "admin";
    public static final String TOKEN_KEY = "gixor-login";
    public static final Character EFFECTIVE = '1';
    public static final String RANK_KEY = "user-rank";
    public static final String TRENDY_REPO_KEY = "trendy-repos";
    public static final String TRENDY_USER_KEY = "trendy-developers";
    public static final String ERROR = "errors";
}
