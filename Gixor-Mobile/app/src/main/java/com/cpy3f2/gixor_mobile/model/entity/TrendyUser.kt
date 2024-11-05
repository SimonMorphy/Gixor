package com.cpy3f2.gixor_mobile.model.entity
import com.google.gson.annotations.SerializedName

/**
 * @author : sadSmile
 * @description :
 * @last : 2024-11-05 11:01
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 11:01
 */
data class TrendyUser(
    @SerializedName("username")
    val username: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("repoName")
    val repoName: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("description")
    val description: String
)
