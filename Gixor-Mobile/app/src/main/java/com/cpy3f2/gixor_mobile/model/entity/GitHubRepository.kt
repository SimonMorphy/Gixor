package com.cpy3f2.gixor_mobile.model.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GitHubRepository(
    val githubId: Long? = null,
    val name: String? = null,

    val fullName: String? = null,
    val owner: Owner? = null,
    val license: License? = null,
    val description: String? = null,
    val language: String? = null,

    @SerializedName("stargazers_count")
    val stargazersCount: Int? = null,

    @SerializedName("forks_count")
    val forksCount: Int? = null,
    val visibility: String? = null,
    val fork: Boolean? = null,

    @SerializedName("open_issues")
    val openIssues: Int? = null,
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

    @SuppressLint("NewApi")
    fun getCreatedDateTime(): LocalDateTime {
        return LocalDateTime.parse(createdAt?.replace("Z", ""))
    }

    @SuppressLint("NewApi")
    fun getUpdatedDateTime(): LocalDateTime {
        return LocalDateTime.parse(updatedAt?.replace("Z", ""))
    }

    @SuppressLint("NewApi")
    fun getPushedDateTime(): LocalDateTime {
        return LocalDateTime.parse(pushedAt?.replace("Z", ""))
    }
}