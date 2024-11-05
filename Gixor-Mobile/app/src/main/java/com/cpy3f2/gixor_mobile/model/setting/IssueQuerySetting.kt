package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * Issue查询设置
 * 用于配置Issue列表的查询参数
 */
open class IssueQuerySetting(
    @SerializedName("state")
    open val state: String? = null,

    @SerializedName("filter")
    open val filter: String? = null,

    @SerializedName("labels")
    open val labels: String? = null,

    @SerializedName("collab")
    open val collab: Boolean? = null,

    @SerializedName("orgs")
    open val orgs: Boolean? = null,

    @SerializedName("owned")
    open val owned: Boolean? = null,

    @SerializedName("pulls")
    open val pulls: Boolean? = null,

    override val perPage: Int? = null,
    override val page: Int? = null,
    override val sort: String? = null,
    override val direction: String? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    /**
     * Issue状态类型
     */
    object State {
        const val OPEN = "open"
        const val CLOSED = "closed"
        const val ALL = "all"
    }

    /**
     * Issue过滤器类型
     */
    object Filter {
        const val ASSIGNED = "assigned"
        const val CREATED = "created"
        const val MENTIONED = "mentioned"
        const val SUBSCRIBED = "subscribed"
        const val ALL = "all"
    }

    /**
     * Issue排序类型
     */
    object Sort {
        const val CREATED = "created"
        const val UPDATED = "updated"
        const val COMMENTS = "comments"
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        state?.let { queryMap["state"] = it }
        filter?.let { queryMap["filter"] = it }
        labels?.let { queryMap["labels"] = it }
        collab?.let { queryMap["collab"] = it.toString() }
        orgs?.let { queryMap["orgs"] = it.toString() }
        owned?.let { queryMap["owned"] = it.toString() }
        pulls?.let { queryMap["pulls"] = it.toString() }
        return queryMap
    }
}