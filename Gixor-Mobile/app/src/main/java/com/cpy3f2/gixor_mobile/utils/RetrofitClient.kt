package com.cpy3f2.gixor_mobile.utils

import com.cpy3f2.gixor_mobile.io.HttpBaseService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by lya
 * describe:retrofit封装
 */

class RetrofitClient {
    companion object {
        private fun createGson(): Gson {
            return GsonBuilder()
                .setLenient() // 设置宽松解析
                .create()
        }

        //首先创建一个OkHttpClient对象
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 连接超时时间
            .readTimeout(30, TimeUnit.SECONDS)     // 读取超时时间
            .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时时间
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()


        //创建Retrofit对象
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(HttpBaseService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .client(okHttpClient)
            .build()

        //创建接口对象
        val httpBaseService = retrofit.create(HttpBaseService::class.java)
    }
}