package com.cpy3f2.gixor_mobile.model.entity

import com.google.gson.annotations.SerializedName


data class Issue(
    val id: Long? = null,
    val number: Int? = null,
    val state: String? = null,
    val title: String? = null,
    val body: String? = null,
    val user: GitHubUser? = null,
    val labels: List<Label>? = null,
    val assignee: GitHubUser? = null,
    val assignees: List<GitHubUser>? = null,
    val milestone: Milestone? = null,
    val locked: Boolean? = null,

    val activeLockReason: String? = null,
    val comments: Int? = null,

    @SerializedName("closed_at")
    val closedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,
    val repository: GitHubRepository? = null
) {

    data class Label(
        val id: Long? = null,
        val name: String? = null,
        val description: String? = null,
        val color: String? = null,
        val defaultLabel: Boolean? = null
    )


    data class Milestone(
        val id: Long? = null,
        val number: Int? = null,
        val state: String? = null,
        val title: String? = null,
        val description: String? = null,
        val creator: GitHubUser? = null,

        @SerializedName("open_issues")
        val openIssues: Int? = null,

        @SerializedName("closed_issues")
        val closedIssues: Int? = null,

        @SerializedName("created_at")
        val createdAt: String? = null,

        @SerializedName("updated_at")
        val updatedAt: String? = null,

        @SerializedName("closed_at")
        val closedAt: String? = null,

        @SerializedName("due_on")
        val dueOn: String? = null
    )
}