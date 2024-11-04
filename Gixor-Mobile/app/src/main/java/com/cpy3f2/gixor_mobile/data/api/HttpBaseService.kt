package com.cpy3f2.gixor_mobile.data.api

import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import retrofit2.http.*

interface HttpBaseService {
    companion object {
        const val BASE_URL = "http://1024.viwipiediema.com:10102"
    }

    /**
     * 获取用户信息接口
     */
    // GitHub登录用户信息接口
    @GET("/sys/user")
    suspend fun getMyUserInfo(
        @Header("gixor-login") tokenValue: String
    ): ResultData<GitHubUser>
    //GitHub指定用户信息接口
    @GET("/sys/user/{username}")
    suspend fun getGitHubUserInfo(
        @Path("username") username: String
    ): ResultData<GitHubUser>

    /***
     * star接口
     */
    @PUT("/gith/star/{username}/{repo}")
    suspend fun starRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Any>

    //取消star
    @DELETE("/gith/star/{username}/{repo}")
    suspend fun unStarRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Any>

    //查看某个项目是不是收藏了
    @GET("/gith/star/{username}/{repo}")
    suspend fun isStarRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Boolean>

    /***
     * 仓库接口
     */
    //查看本人收藏的仓库
    @GET("/gith/repo/starred")
    suspend fun getMyStarRepoList(
        @QueryMap params: Map<String, String>,
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<GitHubRepository>>

    //查看本人的仓库
    @GET("/gith/repo")
    suspend fun getMyRepoList(
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看某人收藏的仓库
    @GET("/gith/repo/starred/{username}")
    suspend fun getUserStarRepoList(
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看某人的仓库
    @GET("/gith/repo/{username}")
    suspend fun getUserRepoList(
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //获得推荐的项目
    @GET("/gith/repo/trendy")
    suspend fun getTrendyRepoList(): ResultData<List<TrendyRepository>>

    /**
     *Milestone相关
     */
    //获取指定仓库的Milestone列表
    @GET("/gith/issue/{owner}/{repo}/milestones")
    suspend fun getMilestoneList(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<Any>>

    //获取指定仓库的Milestone详情
    @GET("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun getMilestoneDetail(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    //创建里程碑
    @POST("/gith/issue/{owner}/{repo}/milestones")
    suspend fun createMilestone(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //更新里程碑
    @PATCH("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun updateMilestone(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>,
        @Body body: Any
    ): ResultData<Any>

    //删除里程碑
    @DELETE("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun deleteMilestone(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    /**
     * issue 相关
     */

    //向指定仓库添加issue
    @POST("/gith/issue/{owner}/{repo}")
    suspend fun createIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //向指定仓库更新issue
    @PATCH("/gith/issue/{owner}/{repo}")
    suspend fun updateIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //锁定指定issue
    @POST("/gith/issue/{owner}/{repo}/{number}")
    suspend fun lockIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long,
        @Body body: Any
    ): ResultData<Any>

    //解锁指定 issue
    @DELETE("/gith/issue/{owner}/{repo}/{number}")
    suspend fun unlockIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long
    ): ResultData<Any>

    //获取指定issue
    @GET("/gith/issue/{owner}/{repo}")
    suspend fun getIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    //获取单个issue的详情
    @GET("/gith/issue/{owner}/{repo}/{number}")
    suspend fun getIssueDetail(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long
    ): ResultData<Any>

    //获取当前用户的issue
    @GET("/gith/issue")
    suspend fun getMyIssueList(
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    //获取指派给当前用户的issue
    @GET("/gith/issue/assigned")
    suspend fun getMyAssignIssueList(
        @QueryMap params: Map<String, String>
    ): ResultData<Any>
}