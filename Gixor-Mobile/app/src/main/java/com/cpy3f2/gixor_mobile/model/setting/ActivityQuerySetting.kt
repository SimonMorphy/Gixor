package com.cpy3f2.gixor_mobile.model.setting

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 活动查询设置
 * 继承基本查询设置，添加活动相关的查询参数
 */
open class ActivityQuerySetting(
    open val all: Boolean? = null,
    open val participating: Boolean? = null,
    open val since: LocalDateTime? = null,
    open val before: LocalDateTime? = null,
    perPage: Int? = null,
    page: Int? = null,
    sort: String? = null,
    direction: String? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    companion object {
        val DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        all?.let { queryMap["all"] = it.toString() }
        participating?.let { queryMap["participating"] = it.toString() }
        since?.let { queryMap["since"] = it.format(DATE_FORMATTER) }
        before?.let { queryMap["before"] = it.format(DATE_FORMATTER) }
        return queryMap
    }
}