package com.cpy3f2.gixor_mobile.model.entity


data class IssueDTO(
    val title: String? = null,
    val body: String? = null,
    val assignees: List<String>? = null,

    val milestone: Long? = null,
    
    val labels: List<String>? = null,
    val state: String? = null,

    val stateReason: String? = null
)