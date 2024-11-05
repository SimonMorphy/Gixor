package com.cpy3f2.gixor_mobile.model.entity
import java.math.BigDecimal
import com.google.gson.annotations.SerializedName

data class GitHubUser(
    @SerializedName("id")
    val githubId: String = "",
    
    @SerializedName("login")
    val login: String = "",
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    
    @SerializedName("html_url")
    val htmlUrl: String = "",
    
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
    
    @SerializedName("public_repos")
    val publicRepos: Int = 0,
    
    @SerializedName("followers")
    val followers: Int = 0,
    
    @SerializedName("following")
    val following: Int = 0,
    
    @SerializedName("followedByCurrentUser")
    val followedByCurrentUser: Boolean? = null,
    
    @SerializedName("isCurrentUser")
    val isCurrentUser: Boolean? = null,
    
    @SerializedName("totalStars")
    val totalStars: Int? = null,
    
    @SerializedName("totalCommits")
    val totalCommits: Int? = null,
    
    @SerializedName("totalPRs")
    val totalPRs: Int? = null,
    
    @SerializedName("totalIssues")
    val totalIssues: Int? = null,
    
    @SerializedName("contributedTo")
    val contributedTo: Int? = null,
    
    @SerializedName("grade")
    val grade: String? = null,
    
    @SerializedName("score")
    val score: BigDecimal? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
)