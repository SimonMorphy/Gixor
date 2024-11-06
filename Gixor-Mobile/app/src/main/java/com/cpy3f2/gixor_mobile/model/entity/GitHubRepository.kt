package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName

data class GitHubRepository(
    @SerializedName("id")
    val githubId: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerializedName("forks_count")
    val forksCount: Int? = null,
    @SerializedName("open_issues")
    val openIssues: Int? = null,
    @SerializedName("watchers_count")
    val watchersCount:Int? = null,
    val owner: Owner? = null,
    val license: License? = null,
    val visibility: String? = null,
    val fork: Boolean? = null,
    val htmlUrl: String? = null,
    val topics: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val pushedAt: String? = null,
    val size: Int? = null,
    val defaultBranch: String? = null
) {

    data class Owner(
        val githubId: Long? = null,
        val name: String? = null,
        val avatarUrl: String? = null,
        val htmlUrl: String? = null,
        val type: String? = null
    )

    data class License(
        val name: String? = null,
        val spdId: String? = null,
        val key: String? = null,
        val url: String? = null
    )
}