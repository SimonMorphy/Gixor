package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 仓库查询设置
 * 用于配置仓库列表的查询参数
 */
data class RepositoryQuerySetting(
    @SerializedName("visibility")
    val visibility: String? = null,
    
    @SerializedName("affiliation")
    val affiliation: String? = null,
    
    @SerializedName("type")
    val type: String? = null,
    
    @SerializedName("language")
    val language: String? = null,
    
    @SerializedName("archived")
    val archived: Boolean? = null,
    
    @SerializedName("fork")
    val fork: Boolean? = null,
    
    override val perPage: Int? = null,
    override val page: Int? = null,
    override val sort: String? = null,
    override val direction: String? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        visibility?.let { queryMap["visibility"] = it }
        affiliation?.let { queryMap["affiliation"] = it }
        type?.let { queryMap["type"] = it }
        language?.let { queryMap["language"] = it }
        archived?.let { queryMap["archived"] = it.toString() }
        fork?.let { queryMap["fork"] = it.toString() }
        return queryMap
    }

    /**
     * 仓库可见性类型
     */
    object Visibility {
        const val ALL = "all"
        const val PUBLIC = "public"
        const val PRIVATE = "private"
    }

    /**
     * 仓库类型
     */
    object Type {
        const val ALL = "all"
        const val OWNER = "owner"
        const val PUBLIC = "public"
        const val PRIVATE = "private"
        const val MEMBER = "member"
    }

    /**
     * 仓库归属关系
     */
    object Affiliation {
        const val OWNER = "owner"
        const val COLLABORATOR = "collaborator"
        const val ORGANIZATION_MEMBER = "organization_member"
    }
}