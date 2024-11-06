package com.cpy3f2.gixor_mobile.model.entity

data class RankUser(
    val id: Int,
    val name: String,
    val score: Int,
    val domains: List<String>,
    val nationality: String
)