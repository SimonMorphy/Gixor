package com.cpy3f2.gixor_mobile.data.api

import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.Issue
import com.cpy3f2.gixor_mobile.model.entity.PullRequest
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
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

    //获取用户排行榜
    @GET("/sys/user/rank")
    suspend fun getUserRank(
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubUser>>

    //获取热点用户
    @GET("/sys/user/trendy")
    suspend fun getHotUserList(
    ): ResultData<List<SimpleUser>>



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
        @Header("gixor-login") tokenValue: String,
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

    //获得热点项目
    @GET("/sys/repo/trendy")
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
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //更新里程碑
    @PATCH("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun updateMilestone(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>,
        @Body body: Any
    ): ResultData<Any>

    //删除里程碑
    @DELETE("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun deleteMilestone(
        @Header("gixor-login") tokenValue: String,
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
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //向指定仓库更新issue
    @PATCH("/gith/issue/{owner}/{repo}")
    suspend fun updateIssue(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Any
    ): ResultData<Any>

    //锁定指定issue
    @POST("/gith/issue/{owner}/{repo}/{number}")
    suspend fun lockIssue(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long,
        @Body body: Any
    ): ResultData<Any>

    //解锁指定 issue
    @DELETE("/gith/issue/{owner}/{repo}/{number}")
    suspend fun unlockIssue(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long
    ): ResultData<Any>

    //获取指定仓库的issue
    @GET("/gith/issue/{owner}/{repo}")
    suspend fun getRepoIssues(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<Issue>>

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
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    //获取指派给当前用户的issue
    @GET("/gith/issue/assigned")
    suspend fun getMyAssignIssueList(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<Any>

    /**
     * Watch相关
     */
    //获取指定仓库的关注者列表
    @GET("/gith/sub/repo/{owner}/{repo}")
    suspend fun getSubscriberList(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<List<Any>>

    //查看是否订阅了指定仓库
    @GET("/gith/sub/{owner}/{repo}")
    suspend fun isSubscribed(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Boolean>

    //获取当前用户的关注列表
    @GET("/gith/sub/watching")
    suspend fun getMySubscribedList(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<Any>>

    //获取指定用户关注的仓库列表
    @GET("/gith/sub/watching/{username}")
    suspend fun getUserSubscribedList(
        @Path("username") username: String
    ): ResultData<List<GitHubRepository>>

    //取消订阅指定仓库
    @DELETE("/gith/sub/{owner}/{repo}")
    suspend fun unsubscribeRepo(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Any>

    //订阅指定仓库
    @PUT("/gith/sub/{owner}/{repo}")
    suspend fun subscribeRepo(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Any>

    /**
     * follow相关
     */
    //获取当前用户的关注者列表
    @GET("/gith/follow/following")
    suspend fun getMyFollowing(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<SimpleUser>>

    //获取指定用户的关注者列表
    @GET("/gith/follow//{username}following")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ): ResultData<List<SimpleUser>>

    //获取当前用户的粉丝列表
    @GET("/gith/follow/followers")
    suspend fun getMyFollowers(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<SimpleUser>>

    //获取指定用户的粉丝列表
    @GET("/gith/follow/{username}followers")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ): ResultData<List<SimpleUser>>

    //获取当前用户是否关注了指定用户
    @GET("/gith/follow/{username}")
    suspend fun isFollowing(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Boolean>

    //关注指定用户
    @PUT("/gith/follow/{username}")
    suspend fun followUser(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Any>

    //取关指定用户
    @DELETE("/gith/follow/{username}")
    suspend fun unfollowUser(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Any>

    /**
     * pr相关
     */
    //获取指定仓库的pr列表
    @GET("/gith/pr/{owner}/{repo}")
    suspend fun getRepoPrList(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<PullRequest>>

    //获取指定的pr
    @GET("/gith/pr/{owner}/{repo}/{number}")
    suspend fun getPrDetail(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long
    ): ResultData<PullRequest>

}