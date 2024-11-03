package com.cpy3f2.gixor_mobile.network.interceptor

import com.cpy3f2.gixor_mobile.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 从 PreferencesManager 获取 token
        val token = MyApplication.preferencesManager.getToken()
        
        // 构建新的请求，添加 header
        val newRequest = originalRequest.newBuilder()
            .header("gixor-login", token ?: "") // 如果token为null则使用空字符串
            .build()
        
        return chain.proceed(newRequest)
    }
} 