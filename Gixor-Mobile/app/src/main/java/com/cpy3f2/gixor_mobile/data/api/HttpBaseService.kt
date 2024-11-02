package com.cpy3f2.gixor_mobile.data.api


import GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.Token
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
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


    //为指定仓库Star
    @PUT("/{owner}/{repo}")
    suspend fun starRepo(@Query("owner") owner: String, @Query("repo") repo : String)

    //为指定仓库取消Star
    @DELETE("/{owner}/{repo}")
    suspend fun unStarRepo(@Query("owner") owner: String, @Query("repo") repo : String)

    //判断当前用户是否收藏了指定仓库
    @GET("/{owner}/{repo}")
    suspend fun isStarRepo(@Query("owner") owner: String, @Query("repo") repo : String):ResultData<Boolean>

    //获取当前用户收藏的仓库
    @GET
    suspend fun getStarRepoList()

    //获取指定用户收藏的仓库
    @GET("/{username}")
    suspend fun getUserStarRepoList(@Query("username") username: String)
}