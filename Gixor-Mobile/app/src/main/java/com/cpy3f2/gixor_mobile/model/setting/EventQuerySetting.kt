package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 事件查询设置
 * 用于配置事件列表的查询参数
 */
data class EventQuerySetting(
    @SerializedName("include_public")
    val includePublic: Boolean? = null,
    
    @SerializedName("include_private")
    val includePrivate: Boolean? = null,

    override val perPage: Int? = null,
    override val page: Int? = null,
    override val sort: String? = null,
    override val direction: String? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        includePublic?.let { queryMap["include_public"] = it.toString() }
        includePrivate?.let { queryMap["include_private"] = it.toString() }
        return queryMap
    }

    /**
     * 事件排序类型
     */
    object Sort {
        const val CREATED = "created"
        const val UPDATED = "updated"
    }

    /**
     * 构建器类
     */
    class Builder {
        private var includePublic: Boolean? = null
        private var includePrivate: Boolean? = null
        private var perPage: Int? = null
        private var page: Int? = null
        private var sort: String? = null
        private var direction: String? = null

        fun includePublic(includePublic: Boolean) = apply { 
            this.includePublic = includePublic 
        }
        
        fun includePrivate(includePrivate: Boolean) = apply { 
            this.includePrivate = includePrivate 
        }
        
        fun perPage(perPage: Int) = apply { 
            this.perPage = perPage 
        }
        
        fun page(page: Int) = apply { 
            this.page = page 
        }
        
        fun sort(sort: String) = apply { 
            this.sort = sort 
        }
        
        fun direction(direction: String) = apply { 
            this.direction = direction 
        }

        /**
         * 设置包含的事件类型
         * @param public 是否包含公开事件
         * @param private 是否包含私有事件
         */
        fun includeEvents(public: Boolean, private: Boolean) = apply {
            this.includePublic = public
            this.includePrivate = private
        }

        fun build() = EventQuerySetting(
            includePublic = includePublic,
            includePrivate = includePrivate,
            perPage = perPage,
            page = page,
            sort = sort,
            direction = direction
        )
    }

    companion object {
        fun builder() = Builder()

        /**
         * 创建一个只包含公开事件的查询设置
         */
        fun publicOnly(
            perPage: Int? = null,
            page: Int? = null
        ) = builder()
            .includeEvents(public = true, private = false)
            .perPage(perPage ?: 30)
            .page(page ?: 1)
            .build()

        /**
         * 创建一个包含所有事件的查询设置
         */
        fun all(
            perPage: Int? = null,
            page: Int? = null
        ) = builder()
            .includeEvents(public = true, private = true)
            .perPage(perPage ?: 30)
            .page(page ?: 1)
            .build()
    }
}