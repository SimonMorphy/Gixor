package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * @author : lya
 * @description :pr
 * @last : 2024-11-04 22:57
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 22:57
 */
data class PullRequest(
    val id: Long? = null,
    val number: Int? = null,
    val state: String? = null,
    val title: String? = null,
    val body: String? = null,
    
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    
    val user: SimpleUser? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    
    @SerializedName("closed_at")
    val closedAt: String? = null,
    
    @SerializedName("merged_at")
    val mergedAt: String? = null,
    
    @SerializedName("draft")
    val isDraft: Boolean? = null,
    
    val head: PullRequestRef? = null,
    val base: PullRequestRef? = null
) {
    /**
     * Pull Request 引用信息
     */
    data class PullRequestRef(
        val label: String? = null,
        val ref: String? = null,
        val sha: String? = null
    )
}