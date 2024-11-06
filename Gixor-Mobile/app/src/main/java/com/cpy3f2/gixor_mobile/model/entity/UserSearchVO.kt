package com.cpy3f2.gixor_mobile.model.entity
import com.google.gson.annotations.SerializedName;
/**
 * @author : lya
 * @description :
 * @last : 2024-11-06 16:47
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 16:47
 */
data class UserSearchVO(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: List<SimpleUser>
)
