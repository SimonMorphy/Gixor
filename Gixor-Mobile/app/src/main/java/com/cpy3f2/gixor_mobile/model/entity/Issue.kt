package com.cpy3f2.gixor_mobile.model.entity

import java.time.LocalDateTime

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

    val closedAt: LocalDateTime? = null,

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,
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

        val openIssues: Int? = null,

        val closedIssues: Int? = null,

        val createdAt: LocalDateTime? = null,

        val updatedAt: LocalDateTime? = null,

        val closedAt: LocalDateTime? = null,

        val dueOn: LocalDateTime? = null
    )
}