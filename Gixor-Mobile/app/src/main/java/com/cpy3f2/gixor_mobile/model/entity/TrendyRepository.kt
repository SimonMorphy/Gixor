package com.cpy3f2.gixor_mobile.model.entity

/**
 * @author : lya
 * @description :推荐的仓库
 * @last : 2024-11-03 17:31
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 17:31
 */
data class TrendyRepository(

    val name: String,
    val author: String,
    val avatar: String,
    val url: String?,
    val description: String?,
    val language: String?,
    val languageColor: String?,
    val stars: Int?,
    val forks: Int?,
    val currentPeriodStars: Int?,
    val builtBy: List<Contributor>?
)

data class Contributor(
    val username: String,
    val href: String,
    val avatar: String
)