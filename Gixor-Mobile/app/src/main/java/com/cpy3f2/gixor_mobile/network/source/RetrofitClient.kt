package com.cpy3f2.gixor_mobile.network.source

import com.cpy3f2.gixor_mobile.MyApplication
import com.cpy3f2.gixor_mobile.data.api.HttpBaseService
import com.cpy3f2.gixor_mobile.model.converter.LocalDateTimeAdapter
import com.cpy3f2.gixor_mobile.network.interceptor.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * Created by lya
 * describe:retrofit数据源
 */

class RetrofitClient {
    companion object {
        // 设置宽松解析，并添加 LocalDateTime 适配器
        private fun createGson(): Gson {
            return GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                .create()
        }

        // 创建认证拦截器
        private val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val token = MyApplication.preferencesManager.getToken()
            
            val request = if (!token.isNullOrEmpty()) {
                original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                original
            }
            
            chain.proceed(request)
        }

        // 创建日志拦截器
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 创建 OkHttpClient
        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
        }
        private fun createBaseOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
        }

        // 创建 基础Retrofit 实例
        private val baseRetrofit = retrofit2.Retrofit.Builder()
            .baseUrl(HttpBaseService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .client(createBaseOkHttpClient())
            .build()

        // 创建服务接口实例
        val httpBaseService = baseRetrofit.create(HttpBaseService::class.java)
    }
}