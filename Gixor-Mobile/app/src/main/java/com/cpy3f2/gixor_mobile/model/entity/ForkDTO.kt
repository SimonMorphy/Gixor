package com.cpy3f2.Gixor.Domain.DTO

import com.google.gson.annotations.SerializedName;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 19:28
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 19:28
 */

data class ForkDTO(
    @SerializedName("organization")
    val organization: String,
    @SerializedName("repository")
    val name: String,
    @SerializedName("defaultBranchOnly")
    val defaultBranchOnly: Boolean? = null
)
