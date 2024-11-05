package com.cpy3f2.gixor_mobile.model.entity


data class IssueDTO(
    val title: String? = null,
    val body: String? = null,

    //指派人
    val assignees: List<String>? = null,

    val milestone: Long? = null,

    //标签
    val labels: List<String>? = null,
    val state: String? = null,

    val stateReason: String? = null
)