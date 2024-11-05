package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 里程碑查询设置
 * 继承自Issue查询设置，添加里程碑特有的查询参数
 */
class MilestoneQuerySetting(
    @SerializedName("sort")
    val sortBy: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    // 继承自IssueQuerySetting的属性
    override val state: String? = null,
    override val filter: String? = null,
    override val labels: String? = null,
    override val collab: Boolean? = null,
    override val orgs: Boolean? = null,
    override val owned: Boolean? = null,
    override val pulls: Boolean? = null,
    override val perPage: Int? = null,
    override val page: Int? = null,
    override val direction: String? = null
) : IssueQuerySetting(
    state = state,
    filter = filter,
    labels = labels,
    collab = collab,
    orgs = orgs,
    owned = owned,
    pulls = pulls,
    perPage = perPage,
    page = page,
    sort = sortBy,
    direction = direction
) {

    /**
     * 里程碑排序字段
     */
    object SortBy {
        const val DUE_DATE = "due_date"
        const val COMPLETENESS = "completeness"
        const val CREATED = "created"
        const val UPDATED = "updated"
    }

    /**
     * 里程碑状态
     */
    object State {
        const val OPEN = "open"
        const val CLOSED = "closed"
        const val ALL = "all"
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        sortBy?.let { queryMap["sort"] = it }
        description?.let { queryMap["description"] = it }
        return queryMap
    }

    /**
     * 构建器类
     */
    class Builder {
        private var sortBy: String? = null
        private var description: String? = null
        private var state: String? = null
        private var filter: String? = null
        private var labels: String? = null
        private var collab: Boolean? = null
        private var orgs: Boolean? = null
        private var owned: Boolean? = null
        private var pulls: Boolean? = null
        private var perPage: Int? = null
        private var page: Int? = null
        private var direction: String? = null

        fun sortBy(sortBy: String) = apply { this.sortBy = sortBy }
        fun description(description: String) = apply { this.description = description }
        fun state(state: String) = apply { this.state = state }
        fun filter(filter: String) = apply { this.filter = filter }
        fun labels(labels: String) = apply { this.labels = labels }
        fun collab(collab: Boolean) = apply { this.collab = collab }
        fun orgs(orgs: Boolean) = apply { this.orgs = orgs }
        fun owned(owned: Boolean) = apply { this.owned = owned }
        fun pulls(pulls: Boolean) = apply { this.pulls = pulls }
        fun perPage(perPage: Int) = apply { this.perPage = perPage }
        fun page(page: Int) = apply { this.page = page }
        fun direction(direction: String) = apply { this.direction = direction }

        fun build() = MilestoneQuerySetting(
            sortBy = sortBy,
            description = description,
            state = state,
            filter = filter,
            labels = labels,
            collab = collab,
            orgs = orgs,
            owned = owned,
            pulls = pulls,
            perPage = perPage,
            page = page,
            direction = direction
        )
    }

    companion object {
        fun builder() = Builder()

        /**
         * 创建一个默认的里程碑查询设置
         */
        fun default() = builder()
            .state(State.OPEN)
            .sortBy(SortBy.DUE_DATE)
            .perPage(30)
            .page(1)
            .build()

        /**
         * 查询即将到期的里程碑
         */
        fun upcoming() = builder()
            .state(State.OPEN)
            .sortBy(SortBy.DUE_DATE)
            .direction("asc")
            .perPage(10)
            .build()

        /**
         * 查询最近更新的里程碑
         */
        fun recentlyUpdated() = builder()
            .sortBy(SortBy.UPDATED)
            .direction("desc")
            .perPage(10)
            .build()

        /**
         * 查询完成度最高的里程碑
         */
        fun mostComplete() = builder()
            .state(State.OPEN)
            .sortBy(SortBy.COMPLETENESS)
            .direction("desc")
            .perPage(10)
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MilestoneQuerySetting) return false
        if (!super.equals(other)) return false

        if (sortBy != other.sortBy) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (sortBy?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}