package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

/**
 * 通知查询设置
 * 继承活动查询设置，添加通知特有的查询参数
 */
data class NotificationQuerySetting(
    @SerializedName("all")
    override val all: Boolean? = null,

    @SerializedName("last_read_at")
    val lastReadAt: LocalDateTime? = null,

    @SerializedName("reason")
    val reason: String? = null,

    @SerializedName("unread")
    val isUnread: Boolean? = null,

    @SerializedName("before")
    override val before: LocalDateTime? = null,

    @SerializedName("subject")
    val subject: String? = null,

    @SerializedName("repository_full_name")
    val repositoryFullName: String? = null,

    override val participating: Boolean? = null,
    override val since: LocalDateTime? = null,
    override val perPage: Int? = null,
    override val page: Int? = null,
    override val sort: String? = null,
    override val direction: String? = null
) : ActivityQuerySetting(
    all = all,
    participating = participating,
    since = since,
    before = before,
    perPage = perPage,
    page = page,
    sort = sort,
    direction = direction
) {
    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        lastReadAt?.let { queryMap["last_read_at"] = it.format(DATE_FORMATTER) }
        reason?.let { queryMap["reason"] = it }
        isUnread?.let { queryMap["unread"] = it.toString() }
        subject?.let { queryMap["subject"] = it }
        repositoryFullName?.let { queryMap["repository_full_name"] = it }
        return queryMap
    }

}