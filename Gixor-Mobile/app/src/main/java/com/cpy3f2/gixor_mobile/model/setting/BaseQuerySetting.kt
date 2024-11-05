package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 查询设置基类
 * 包含分页、排序等基本查询参数
 */
open class BaseQuerySetting(
    @SerializedName("per_page")
    open val perPage: Int? = null,

    @SerializedName("page")
    open val page: Int? = null,

    @SerializedName("sort")
    open val sort: String? = null,

    @SerializedName("direction")
    open val direction: String? = null
) {
    /**
     * 转换为查询参数Map
     */
    open fun toQueryMap(): MutableMap<String, String> {
        val queryMap = mutableMapOf<String, String>()
        perPage?.let { queryMap["per_page"] = it.toString() }
        page?.let { queryMap["page"] = it.toString() }
        sort?.let { queryMap["sort"] = it }
        direction?.let { queryMap["direction"] = it }
        return queryMap
    }
}