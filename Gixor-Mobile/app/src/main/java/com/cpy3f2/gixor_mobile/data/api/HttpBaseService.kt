package com.cpy3f2.gixor_mobile.data.api

import com.cpy3f2.gixor_mobile.model.entity.Event
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.Issue
import com.cpy3f2.gixor_mobile.model.entity.IssueComment
import com.cpy3f2.gixor_mobile.model.entity.IssueDTO
import com.cpy3f2.gixor_mobile.model.entity.Notification
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
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<GitHubUser>

    //获取用户排行榜
    @GET("/sys/user/rank")
    suspend fun getUserRank(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubUser>>

    //获取热点用户
    @GET("/sys/user/trendy")
    suspend fun getHotUserList(
        @Header("gixor-login") tokenValue: String,
    ): ResultData<List<SimpleUser>>



    /***
     * star接口
     */
    //收藏
    @PUT("/gith/star/{username}/{repo}")
    suspend fun starRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Unit>

    //取消star
    @DELETE("/gith/star/{username}/{repo}")
    suspend fun unStarRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Unit>

    //查看某个项目是不是收藏了
    @GET("/gith/star/{username}/{repo}")
    suspend fun isStarRepo(
        @Path("username") username: String,
        @Path("repo") repo: String,
        @Header("gixor-login") tokenValue: String
    ): ResultData<Unit>

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
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //查看某人的仓库
    @GET("/gith/repo/{username}")
    suspend fun getUserRepoList(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<GitHubRepository>>

    //获得热点项目
    @GET("/sys/repo/trendy")
    suspend fun getTrendyRepoList(
        @Header("gixor-login") tokenValue: String,
    ): ResultData<List<TrendyRepository>>

    //获取指定仓库的详情
    @GET("/gith/repo/{owner}/{repo}")
    suspend fun getRepoDetail(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<GitHubRepository>


    /**
     *Milestone相关
     */
    //获取指定仓库的Milestone列表
    @GET("/gith/issue/{owner}/{repo}/milestones")
    suspend fun getMilestoneList(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<Issue.Milestone>>

    //获取指定仓库的Milestone详情
    @GET("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun getMilestoneDetail(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>
    ): ResultData<Issue.Milestone>

    //创建里程碑
    @POST("/gith/issue/{owner}/{repo}/milestones")
    suspend fun createMilestone(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Issue.Milestone
    ): ResultData<Unit>

    //更新里程碑
    @PATCH("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun updateMilestone(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>,
        @Body body: Issue.Milestone
    ): ResultData<Unit>

    //删除里程碑
    @DELETE("/gith/issue/{owner}/{repo}/milestones/{milestoneNumber}")
    suspend fun deleteMilestone(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("milestoneNumber") number: Long,
        @QueryMap params: Map<String, String>
    ): ResultData<Unit>

    /**
     * issue 相关
     */

    //向指定仓库添加issue
    @POST("/gith/issue/{owner}/{repo}")
    suspend fun createIssue(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: IssueDTO
    ): ResultData<Unit>

    //向指定仓库更新issue
    @PATCH("/gith/issue/{owner}/{repo}")
    suspend fun updateIssue(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: IssueDTO
    ): ResultData<Unit>

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
    ): ResultData<Unit>

    //获取指定仓库的issue
    @GET("/gith/issue/{owner}/{repo}")
    suspend fun getRepoIssues(
        @Header("gixor-login") tokenValue: String,
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
    ): ResultData<Issue>

    //获取当前用户的issue
    @GET("/gith/issue")
    suspend fun getMyIssueList(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<Issue>

    //获取指派给当前用户的issue
    @GET("/gith/issue/assigned")
    suspend fun getMyAssignIssueList(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<Issue>

    /**
     * Comment 相关
     */
    //获取指定issue的评论列表
    @GET("/gith/issue/{owner}/{repo}/issues/{issue_number}/comments")
    suspend fun getIssueComments(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issue_number") number: Long,
        @QueryMap params: Map<String, String>
    ): ResultData<List<IssueComment>>

    //添加评论
    @POST("/gith/issue/{owner}/{repo}/issues/{issue_number}/comments")
    suspend fun addComment(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issue_number") number: Long,
        @Body body: String
    ): ResultData<Unit>

    //更新评论
    @PATCH("/gith/issue/{owner}/{repo}/comments/{comment_id}")
    suspend fun updateComment(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("comment_id") commentId: Long,
        @Body body: String
    ): ResultData<Unit>

    //删除评论
    @DELETE("/gith/issue/{owner}/{repo}/comments/{comment_id}")
    suspend fun deleteComment(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("comment_id") commentId: Long
    ): ResultData<Unit>

    //获取单个评论
    @GET("/gith/issue/{owner}/{repo}/comments/{comment_id}")
    suspend fun getComment(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("comment_id") commentId: Long
    ): ResultData<IssueComment>

    /**
     * Watch相关
     */
    //获取指定仓库的关注者列表
    @GET("/gith/sub/repo/{owner}/{repo}")
    suspend fun getSubscriberList(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<List<SimpleUser>>

    //查看是否订阅了指定仓库
    @GET("/gith/sub/{owner}/{repo}")
    suspend fun isSubscribed(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Unit>

    //获取当前用户的关注列表
    @GET("/gith/sub/watching")
    suspend fun getMySubscribedList(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<SimpleUser>>

    //获取指定用户关注的仓库列表
    @GET("/gith/sub/watching/{username}")
    suspend fun getUserSubscribedList(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<List<GitHubRepository>>

    //取消订阅指定仓库
    @DELETE("/gith/sub/{owner}/{repo}")
    suspend fun unsubscribeRepo(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Unit>

    //订阅指定仓库
    @PUT("/gith/sub/{owner}/{repo}")
    suspend fun subscribeRepo(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Unit>

    /**
     * follow相关
     */
    //获取当前用户的关注者列表
    @GET("/gith/follow/following")
    suspend fun getMyFollowing(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<SimpleUser>>

    //获取指定用户的关注者列表
    @GET("/gith/follow/{username}/following")
    suspend fun getUserFollowing(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<SimpleUser>>

    //获取当前用户的粉丝列表
    @GET("/gith/follow/followers")
    suspend fun getMyFollowers(
        @Header("gixor-login") tokenValue: String
    ): ResultData<List<SimpleUser>>

    //获取指定用户的粉丝列表
    @GET("/gith/follow/{username}/followers")
    suspend fun getUserFollowers(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<SimpleUser>>

    //获取当前用户是否关注了指定用户
    @GET("/gith/follow/{username}")
    suspend fun isFollowing(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Unit>

    //关注指定用户
    @PUT("/gith/follow/{username}")
    suspend fun followUser(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Unit>

    //取关指定用户
    @DELETE("/gith/follow/{username}")
    suspend fun unfollowUser(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String
    ): ResultData<Unit>

    /**
     * pr相关
     */
    //获取指定仓库的pr列表
    @GET("/gith/pr/{owner}/{repo}")
    suspend fun getRepoPrList(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<PullRequest>>

    //获取指定的pr
    @GET("/gith/pr/{owner}/{repo}/{number}")
    suspend fun getPrDetail(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("number") number: Long
    ): ResultData<PullRequest>

    /**
     * event 相关
     */
    //获取公共动态
    @GET("/gith/event/public")
    suspend fun getPublicEvent(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<Event>>

    //获取指定用户收到接收到的动态
    @GET("/gith/event/users/{username}/received")
    suspend fun getReceivedEvent(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
    ): ResultData<List<Event>>

    //获取与某用户相关的动态
    @GET("/gith/event/users/{username}")
    suspend fun getRelatedEvent(
        @Header("gixor-login") tokenValue: String,
        @Path("username") username: String,
    ): ResultData<List<Event>>

    //获取与某组织相关的动态
    @GET("/gith/event/orgs/{org}")
    suspend fun getOrgEvent(
        @Header("gixor-login") tokenValue: String,
        @Path("org") org: String,
    ): ResultData<List<Event>>

    /**
     * Notification 相关
     */
    //获取通知列表
    @GET("/gith/noti")
    suspend fun getNotificationList(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<Notification>>

    //一键已读所有通知列表
    @PUT("/gith/noti")
    suspend fun getUnreadNotificationList(
        @Header("gixor-login") tokenValue: String,
        @QueryMap params: Map<String, String>
    ):ResultData<List<Notification>>

    /**
     * discussion相关
     */
    //获取指定仓库的讨论列表
//    @GET("/gith/disc/{owner}/{repo}")
//    suspend fun getRepoDiscussionList(
//        @Header("gixor-login") tokenValue: String,
//        @Path("owner") owner: String,
//        @Path("repo") repo: String,
//        @QueryMap params: Map<String, String>
//    ): ResultData<List<Discussion>>

    /**
     * fork 相关
     */
    //获取指定仓库的fork列表
    @GET("/gith/fork/{owner}/{repo}")
    suspend fun getRepoForkUserList(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @QueryMap params: Map<String, String>
    ): ResultData<List<SimpleUser>>

    //fork 指定仓库
    @POST("/gith/fork/{owner}/{repo}")
    suspend fun forkRepo(
        @Header("gixor-login") tokenValue: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): ResultData<Unit>
}