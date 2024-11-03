package com.cpy3f2.gixor_mobile.model.entity

/**
 * @author : lya
 * @description : GitHub用户实体类
 * @last : 2024-10-30 19:43
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 19:43
 */

data class GitHubUser(
    // 基本标识信息
    val githubId: Long? = null,
    val login: String? = null,
    val name: String? = null,
    val displayLogin: String? = null,
    val avatarUrl: String? = null,
    val htmlUrl: String? = null,

    // 个人资料信息
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val hlProfileBio: String? = null,

    // 统计数据
    val publicRepos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,

    // 用户状态
    val followedByCurrentUser: Boolean? = null,
    val isCurrentUser: Boolean? = null,
    val sponsorable: Boolean? = null,

    // 活动统计
    val totalStars: Int? = null,
    val totalCommits: Int? = null,
    val totalPRs: Int? = null,
    val totalIssues: Int? = null,
    val contributedTo: Int? = null,

    // 评分相关
    val grade: String? = null,
    val score: Double? = null,

    // 时间戳
    val createdAt: String? = null,
    val updatedAt: String? = null
)