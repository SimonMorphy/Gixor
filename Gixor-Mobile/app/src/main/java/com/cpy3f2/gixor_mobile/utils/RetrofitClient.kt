package com.cpy3f2.gixor_mobile.utils

import com.cpy3f2.gixor_mobile.io.HttpBaseService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by lya
 * describe:retrofit封装
 */

object RetrofitClient {
    //首先创建一个OkHttpClient对象
    val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30,TimeUnit.SECONDS)
        .build()

    //创建Retrofit对象
    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(HttpBaseService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    //创建接口对象
    val httpBaseService = retrofit.create(HttpBaseService::class.java)
}