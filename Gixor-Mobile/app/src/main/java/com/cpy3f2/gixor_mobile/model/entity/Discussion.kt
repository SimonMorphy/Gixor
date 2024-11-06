package com.cpy3f2.gixor_mobile.model.entity

import java.time.LocalDateTime
import com.google.gson.annotations.SerializedName;

/**
 * @author : lya
 * @description :讨论
 * @last : 2024-11-05 21:47
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 21:47
 */
data class Discussion(
    val title: String,
    val body: String,

    @SerializedName("created_at")
    val createdAt: LocalDateTime,

    @SerializedName("author")
    val authorName: String,

    @SerializedName("category")
    val categoryName: String,

    val comments: MutableList<Comment>
) {
    data class Comment(
        val body: String,
        val authorName: String
    )
}
