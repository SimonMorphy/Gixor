package com.cpy3f2.gixor_mobile.model.entity
import java.math.BigDecimal
import java.time.LocalDateTime

data class GitHubUser(
    val githubId: String = "",
    val login: String = "",
    val name: String? = null,
    val avatarUrl: String = "",
    val htmlUrl: String = "",
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val publicRepos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val followedByCurrentUser: Boolean = false,
    val isCurrentUser: Boolean = false,
    val totalStars: Int = 0,
    val totalCommits: Int = 0,
    val totalPRs: Int = 0,
    val totalIssues: Int = 0,
    val contributedTo: Int = 0,
    val grade: String = "",
    val score: BigDecimal = BigDecimal.ZERO,
    val totalPRsMerged: Long = 0,
    val mergedPRsPercentage: Double = 0.0,
    val totalPRsReviewed: Long = 0,
    val totalDiscussionsStarted: Long = 0,
    val totalDiscussionsAnswered: Long = 0,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)