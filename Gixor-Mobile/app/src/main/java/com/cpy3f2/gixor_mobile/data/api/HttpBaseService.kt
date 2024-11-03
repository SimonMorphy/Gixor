package com.cpy3f2.gixor_mobile.data.api

import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import retrofit2.http.*

interface HttpBaseService {
    companion object {
        const val BASE_URL = "http://1024.viwipiediema.com:10102"
        const val STAR_URL = "$BASE_URL/gith/star/"
        const val REPOSITORY_URL = "$BASE_URL/gith/repo/"
    }

    /**
     * 获取用户信息接口
     */
    // GitHub用户信息接口
    @GET("/gith/user")
    suspend fun getGitHubUserInfo(
        @Header("gixor-login") tokenValue: String
    ): ResultData<GitHubUser>

    /***
     * star接口
     */
    @PUT("/{username}/{repo}")
    suspend fun starRepo(
        @Path("username") username: String,
        @Path("repo") repo: String
    )
    //取消star
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
    suspend fun getStarRepoList(): ResultData<List<GitHubRepository>>  // 替换 Any 为具体的数据类型

    @GET("/users/{username}/starred")
    suspend fun getUserStarRepoList(
        @Path("username") username: String
    ): ResultData<List<GitHubRepository>>  // 替换 Any 为具体的数据类型

    /***
     * 仓库接口
     */
    //查看本人收藏的仓库
    @GET("/starred")
    suspend fun getMyStarRepoList(
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看本人的仓库
    @GET
    suspend fun getMyRepoList(
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看某人收藏的仓库
    @GET("/starred/{username}")
    suspend fun getUserStarRepoList(
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看某人的仓库
    @GET("/{username}")
    suspend fun getUserRepoList(
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

}