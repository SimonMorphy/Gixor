package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName;

/**
 * @author : lya
 * @description :
 * @last : 2024-11-06 16:50
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 16:50
 */

data class RepositorySearchVO(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val repositories: List<GitHubRepository>
)
