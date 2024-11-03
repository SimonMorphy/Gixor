package com.cpy3f2.gixor_mobile.data.api

import GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.Token
import retrofit2.http.*

interface HttpBaseService {
    companion object {
        const val BASE_URL = "http://1024.viwipiediema.com:10102"
        const val STAR_URL = BASE_URL+"/gith/star"
        const val REPOSITORY_URL = BASE_URL+"/gith/repo"
    }

    // GitHub用户信息接口
    @GET("/gith/user")
    suspend fun getGitHubUserInfo(
        @Header("gixor-login") tokenValue: String
    ): ResultData<GitHubUser>

    // Star相关接口
    @PUT("/{username}/{repo}")
    suspend fun starRepo(
        @Path("username") username: String,
        @Path("repo") repo: String
    )

    @DELETE("/{username}/{repo}")
    suspend fun unStarRepo(
        @Path("username") username: String,
        @Path("repo") repo: String
    )
    //查看某个项目是不是收藏了
    @GET("/{username}/{repo}")
    suspend fun isStarRepo(
        @Path("username") username: String,
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