package com.cpy3f2.gixor_mobile.model.entity

data class RankUiState(
    val users: List<RankUser> = emptyList(),
    val filteredUsers: List<RankUser> = emptyList(),
    val domains: List<String> = emptyList(),
    val nationalities: List<String> = emptyList(),
    val selectedDomain: String = "All",
    val selectedNationality: String = "All",
    val currentPage: Int = 1,
    val usersPerPage: Int = 15
)