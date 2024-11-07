package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    @SerializedName("id")
    val githubId: String = "",

    @SerializedName("login")
    val login: String = "",

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("company")
    val company: String? = null,

    @SerializedName("blog")
    val blog: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("followers")
    val followers: Int = 0,

    @SerializedName("following")
    val following: Int = 0,

    //TODO 当更换数据时，需要加上这个字段
    @SerializedName("watchRepos")
    val watchRepos: Int? = null,

    @SerializedName("followedByCurrentUser")
    val followedByCurrentUser: Boolean? = null,

    @SerializedName("isCurrentUser")
    val isCurrentUser: Boolean? = null,

    @SerializedName("totalStars")
    val totalStars: Int = 0,

    @SerializedName("totalCommits")
    val totalCommits: Int = 0,

    @SerializedName("totalPRs")
    val totalPRs: Int = 0,

    @SerializedName("totalPRsMerged")
    val totalPRsMerged: Int = 0,

    @SerializedName("mergedPRsPercentage")
    val mergedPRsPercentage: Double = 0.0,

    @SerializedName("totalPRsReviewed")
    val totalPRsReviewed: Int = 0,

    @SerializedName("totalIssues")
    val totalIssues: Int = 0,

    @SerializedName("totalDiscussionsStarted")
    val totalDiscussionsStarted: Int = 0,

    @SerializedName("totalDiscussionsAnswered")
    val totalDiscussionsAnswered: Int = 0,

    @SerializedName("contributedTo")
    val contributedTo: Int = 0,

    @SerializedName("grade")
    val grade: String = "",

    @SerializedName("score")
    val score: Double = 0.0,

    @SerializedName("majorDomains")
    val majorDomains: List<String>? = null,

    @SerializedName("domainWeights")
    val domainWeights: Map<String, Double>? = null,

    @SerializedName("nation")
    val nation: String = "",

    @SerializedName("avatar_url")
    val avatarUrl: String = "",

    @SerializedName("html_url")
    val htmlUrl: String = "",

    @SerializedName("public_repos")
    val publicRepos: Int = 0,

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
)
