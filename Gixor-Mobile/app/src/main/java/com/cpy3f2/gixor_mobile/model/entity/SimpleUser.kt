package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName

/**
 * @author : lya
 * @description :粉丝 和 博主
 * @last : 2024-11-05 09:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 09:23
 */
data class SimpleUser(
    @SerializedName("id")
    val githubId: String? = null,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    
    @SerializedName("html_url")
    val htmlUrl: String? = null
)