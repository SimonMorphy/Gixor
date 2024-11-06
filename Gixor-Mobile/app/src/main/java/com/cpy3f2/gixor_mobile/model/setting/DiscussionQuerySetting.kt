package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 讨论查询设置
 * 用于配置讨论列表的查询参数
 */
data class DiscussionQuerySetting(
    @SerializedName("first")
    val first: Int? = null,
    
    @SerializedName("category")
    val category: String? = null,
    
    @SerializedName("orderBy")
    val orderBy: String? = null,
    
    @SerializedName("direction")
    override val direction: String? = null,
    
    override val perPage: Int? = null,
    override val page: Int? = null,
    override val sort: String? = null,
    @SerializedName("after")
    val after: String? = null
) : BaseQuerySetting(perPage, page, sort, direction) {

    /**
     * 讨论排序字段
     */
    object OrderBy {
        const val CREATED_AT = "CREATED_AT"
        const val UPDATED_AT = "UPDATED_AT"
        const val COMMENTS = "COMMENTS"
        const val REACTIONS = "REACTIONS"
    }

    /**
     * 排序方向
     */
    object Direction {
        const val ASC = "ASC"
        const val DESC = "DESC"
    }

    /**
     * 常用讨论分类
     */
    object Category {
        const val GENERAL = "General"
        const val IDEAS = "Ideas"
        const val Q_AND_A = "Q&A"
        const val SHOW_AND_TELL = "Show and tell"
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        first?.let { queryMap["first"] = it.toString() }
        category?.let { queryMap["category"] = it }
        orderBy?.let { queryMap["orderBy"] = it }
        return queryMap
    }

    /**
     * 构建器类
     */
    class Builder {
        private var first: Int? = null
        private var category: String? = null
        private var orderBy: String? = null
        private var direction: String? = null
        private var perPage: Int? = null
        private var page: Int? = null
        private var sort: String? = null
        private var after: String? = null

        fun first(first: Int) = apply { this.first = first }
        fun category(category: String) = apply { this.category = category }
        fun orderBy(orderBy: String) = apply { this.orderBy = orderBy }
        fun direction(direction: String) = apply { this.direction = direction }
        fun perPage(perPage: Int) = apply { this.perPage = perPage }
        fun page(page: Int) = apply { this.page = page }
        fun sort(sort: String) = apply { this.sort = sort }
        fun after(after: String) = apply { this.after = after }

        fun build() = DiscussionQuerySetting(
            first = first,
            category = category,
            orderBy = orderBy,
            direction = direction,
            perPage = perPage,
            page = page,
            sort = sort,
            after = after
        )
    }

    companion object {
        fun builder() = Builder()

        /**
         * 创建一个默认的讨论查询设置
         */
        fun default() = builder()
            .first(10)
            .orderBy(OrderBy.UPDATED_AT)
            .direction(Direction.DESC)
            .build()

        /**
         * 创建一个特定分类的讨论查询设置
         */
        fun forCategory(
            category: String,
            first: Int = 10
        ) = builder()
            .category(category)
            .first(first)
            .orderBy(OrderBy.UPDATED_AT)
            .direction(Direction.DESC)
            .build()

        /**
         * 创建一个用于问答的讨论查询设置
         */
        fun forQAndA(first: Int = 10) = forCategory(Category.Q_AND_A, first)
    }
}