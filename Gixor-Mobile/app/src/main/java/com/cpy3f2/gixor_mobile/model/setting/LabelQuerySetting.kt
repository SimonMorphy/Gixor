package com.cpy3f2.gixor_mobile.model.setting

import com.google.gson.annotations.SerializedName

/**
 * 标签查询设置
 * 继承自Issue查询设置，添加标签特有的查询参数
 */
class LabelQuerySetting(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("color")
    val color: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("sort")
    val sortBy: String? = null,
    
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
     * 标签排序字段
     */
    object SortBy {
        const val NAME = "name"
        const val CREATED = "created"
        const val UPDATED = "updated"
    }

    override fun toQueryMap(): MutableMap<String, String> {
        val queryMap = super.toQueryMap()
        name?.let { queryMap["name"] = it }
        color?.let { queryMap["color"] = it }
        description?.let { queryMap["description"] = it }
        sortBy?.let { queryMap["sort"] = it }
        return queryMap
    }

    /**
     * 构建器类
     */
    class Builder {
        private var name: String? = null
        private var color: String? = null
        private var description: String? = null
        private var sortBy: String? = null
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

        fun name(name: String) = apply { this.name = name }
        fun color(color: String) = apply { this.color = color }
        fun description(description: String) = apply { this.description = description }
        fun sortBy(sortBy: String) = apply { this.sortBy = sortBy }
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

        fun build() = LabelQuerySetting(
            name = name,
            color = color,
            description = description,
            sortBy = sortBy,
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
         * 创建一个默认的标签查询设置
         */
        fun default() = builder()
            .sortBy(SortBy.NAME)
            .perPage(30)
            .page(1)
            .build()

        /**
         * 按名称搜索标签
         */
        fun searchByName(name: String) = builder()
            .name(name)
            .sortBy(SortBy.NAME)
            .perPage(30)
            .build()

        /**
         * 按颜色搜索标签
         */
        fun searchByColor(color: String) = builder()
            .color(color)
            .sortBy(SortBy.NAME)
            .perPage(30)
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LabelQuerySetting) return false
        if (!super.equals(other)) return false

        if (name != other.name) return false
        if (color != other.color) return false
        if (description != other.description) return false
        if (sortBy != other.sortBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (color?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (sortBy?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "LabelQuerySetting(name=$name, color=$color, description=$description, sortBy=$sortBy, state=$state, filter=$filter, labels=$labels, collab=$collab, orgs=$orgs, owned=$owned, pulls=$pulls, perPage=$perPage, page=$page, direction=$direction)"
    }
}