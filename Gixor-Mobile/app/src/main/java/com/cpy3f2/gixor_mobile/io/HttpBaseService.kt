package com.cpy3f2.gixor_mobile.io


import GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpBaseService {
    companion object {
        val BASE_URL =  "http://192.168.31.67:8080/ssm/ws/commRest/"
    }
    //suspend关键字修饰方法，表示这是一个挂起函数，可以在协程中使用,然后返回可以直接返回我们想要的实体类
    @GET("/auth/render")
    suspend fun login(@Query("username") username : String, @Query("password") password : String):ResultData<GitHubUser>
}