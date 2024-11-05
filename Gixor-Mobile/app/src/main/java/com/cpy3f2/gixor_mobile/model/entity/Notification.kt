package com.cpy3f2.gixor_mobile.model.entity

import java.time.LocalDateTime

data class Notification(
    val id: String? = null,
    val repository: Repository? = null,
    val subject: Subject? = null,
    val reason: String? = null,
    val unread: Boolean = false,

    val updatedAt: LocalDateTime? = null,

    val lastReadAt: LocalDateTime? = null,
    
    val url: String? = null,

    val subscriptionUrl: String? = null
) {
    data class Repository(
        val id: Long? = null,

        val nodeId: String? = null,
        
        val name: String? = null,

        val fullName: String? = null,
        
        val owner: Owner? = null,
        val isPrivate: Boolean = false,

        val htmlUrl: String? = null,
        
        val description: String? = null
    )

    data class Owner(
        val login: String? = null,
        val id: Long? = null,

        val nodeId: String? = null,

        val avatarUrl: String? = null,

        val htmlUrl: String? = null,
        
        val type: String? = null,

        val siteAdmin: Boolean = false
    )

    data class Subject(
        val title: String? = null,
        val url: String? = null,

        val latestCommentUrl: String? = null,
        
        val type: String? = null
    )
}