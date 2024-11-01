package com.cpy3f2.gixor_mobile.io


import GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.Token
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HttpBaseService {
    companion object {
        val BASE_URL =  "http://182.92.129.140"
    }
    //suspend关键字修饰方法，表示这是一个挂起函数，可以在协程中使用,然后返回可以直接返回我们想要的实体类
    @GET("/auth/render")
    suspend fun login(@Query("username") username : String, @Query("password") password : String):ResultData<Token>

    @GET("/auth/render")
    suspend fun otherLogin():ResultData<Token>

    @GET("/gith/user")
    suspend fun getGitHubUserInfo(@Header("gixor-login") tokenValue : String):ResultData<GitHubUser>
}