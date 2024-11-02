package com.cpy3f2.gixor_mobile.data.api

import GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.Token
import retrofit2.http.*

interface HttpBaseService {
    companion object {
        const val BASE_URL = "http://1024.viwipiediema.com:10102"
    }

    // 登录相关接口
    @GET("/auth/render")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): ResultData<Token>

    @GET("/auth/render")
    suspend fun otherLogin(): ResultData<Token>

    // GitHub用户信息接口
    @GET("/gith/user")
    suspend fun getGitHubUserInfo(
        @Header("gixor-login") tokenValue: String
    ): ResultData<GitHubUser>

    // Star相关接口
    @PUT("/{owner}/{repo}")
    suspend fun starRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    )

    @DELETE("/{owner}/{repo}")
    suspend fun unStarRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    )

    @GET("/{owner}/{repo}")
    suspend fun isStarRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Boolean>

    // Star列表接口
    @GET("/user/starred")
    suspend fun getStarRepoList(): ResultData<List<Any>>  // 替换 Any 为具体的数据类型

    @GET("/users/{username}/starred")
    suspend fun getUserStarRepoList(
        @Path("username") username: String
    ): ResultData<List<Any>>  // 替换 Any 为具体的数据类型
}