package com.cpy3f2.gixor_mobile.model.entity

data class GitHubUserDO(
    val name: String? = null,
    val email: String? = null,
    val blog: String? = null,
    val bio: String? = null,
    val company: String? = null,
    val location: String? = null,

    val twitterUsername: String? = null,
    val hireable: Boolean? = null
)