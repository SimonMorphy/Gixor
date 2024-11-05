package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * @author : lya
 * @description : 评论
 * @last : 2024-11-04 23:16
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 23:16
 */
data class IssueComment(
    val id: Long? = null,
    
    @SerializedName("node_id")
    val nodeId: String? = null,
    
    val url: String? = null,
    
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    
    val body: String? = null,
    
    val user: GitHubUser? = null,
    
    @SerializedName("created_at")
    val createdAt: LocalDateTime? = null,
    
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime? = null,
    
    @SerializedName("issue_url")
    val issueUrl: String? = null,
    
    @SerializedName("author_association")
    val authorAssociation: String? = null
)