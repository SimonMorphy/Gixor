package com.cpy3f2.gixor_mobile.model.entity

import java.time.LocalDateTime

data class SubscriptionDTO(
    val subscribed: Boolean? = null,
    val ignored: Boolean? = null,
    val reason: String? = null,

    val createdAt: LocalDateTime? = null,
    
    val url: String? = null,

    val repositoryUrl: String? = null
)