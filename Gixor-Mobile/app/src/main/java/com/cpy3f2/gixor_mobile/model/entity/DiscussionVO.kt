package com.cpy3f2.gixor_mobile.model.entity


data class DiscussionVO(
    val nodes: List<Discussion>,
    val pageInfo: PageInfo
) {

    data class PageInfo(
        val hasNextPage: Boolean,
        val endCursor: String
    )
}
