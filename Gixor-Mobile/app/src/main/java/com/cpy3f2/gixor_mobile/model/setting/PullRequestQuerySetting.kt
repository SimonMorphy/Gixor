package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * Pull Request查询设置
 * 用于配置PR列表的查询参数
 */
data class PullRequestQuerySetting(
    @SerializedName("state")
    val state: String? = null,
    
    @SerializedName("head")
    val head: String? = null,
    
    @SerializedName("base")
    val base: String? = null,
    
    @SerializedName("sort")
    override val sort: String? = null,
    
    @SerializedName("direction")
    override val direction: String? = null,
    
    @SerializedName("draft")
    val isDraft: Boolean? = null,
    
    @SerializedName("labels")
    val labels: String? = null,
    
    @SerializedName("milestone")
    val milestone: String? = null,
    
    override val perPage: Int? = null,
    override val page: Int? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    /**
     * PR状态类型
     */
    object State {
        const val OPEN = "open"
        const val CLOSED = "closed"
        const val ALL = "all"
    }

    /**
     * PR排序类型
     */
    object Sort {
        const val CREATED = "created"
        const val UPDATED = "updated"
        const val POPULARITY = "popularity"
        const val LONG_RUNNING = "long-running"
    }

    /**
     * 排序方向
     */
    object Direction {
        const val ASC = "asc"
        const val DESC = "desc"
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        state?.let { queryMap["state"] = it }
        head?.let { queryMap["head"] = it }
        base?.let { queryMap["base"] = it }
        isDraft?.let { queryMap["draft"] = it.toString() }
        labels?.let { queryMap["labels"] = it }
        milestone?.let { queryMap["milestone"] = it }
        return queryMap
    }
}