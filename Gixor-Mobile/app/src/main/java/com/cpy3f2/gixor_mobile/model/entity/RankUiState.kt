package com.cpy3f2.gixor_mobile.model.entity


data class RankUiState(
    val users: List<GitHubUser> = emptyList(),
    val filteredUsers: List<GitHubUser> = emptyList(),
    val domains: List<String> = emptyList(),
    val nationalities: List<String> = emptyList(),
    val selectedDomain: String = "All",
    val selectedNationality: String = "All",
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)
