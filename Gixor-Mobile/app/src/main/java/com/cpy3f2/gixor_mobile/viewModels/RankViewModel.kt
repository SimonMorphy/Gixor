package com.cpy3f2.gixor_mobile.viewModels

import androidx.lifecycle.ViewModel
import com.cpy3f2.gixor_mobile.model.entity.RankUiState
import com.cpy3f2.gixor_mobile.model.entity.RankUser

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RankViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RankUiState())
    val uiState: StateFlow<RankUiState> = _uiState.asStateFlow()

    init {
        val mockUsers = (1..20).map { id ->
            RankUser(
                id = id,
                name = "User $id",
                score = (1000 - id * 10 + (Math.random() * 20 - 10)).toInt(),
                domains = listOf("Android", "Web", "AI", "iOS").shuffled().take((1..3).random()),
                nationality = listOf(
                    "USA",
                    "China",
                    "Japan",
                    "Korea",
                    "India",
                    "Germany",
                    "France"
                ).random()
            )
        }
        _uiState.value = RankUiState(
            users = mockUsers,
            filteredUsers = mockUsers.sortedByDescending { it.score },
            domains = listOf("All", "Android", "Web", "AI", "iOS"),
            nationalities = listOf(
                "All",
                "USA",
                "China",
                "Japan",
                "Korea",
                "India",
                "Germany",
                "France"
            )
        )
    }

    fun updateDomain(domain: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDomain = domain,
                filteredUsers = filterAndSortUsers(domain, currentState.selectedNationality),
                currentPage = 1
            )
        }
    }

    fun updateNationality(nationality: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedNationality = nationality,
                filteredUsers = filterAndSortUsers(currentState.selectedDomain, nationality),
                currentPage = 1
            )
        }
    }

    private fun filterAndSortUsers(domain: String, nationality: String): List<RankUser> {
        return _uiState.value.users.filter { user ->
            (domain == "All" || user.domains.contains(domain)) &&
                    (nationality == "All" || user.nationality == nationality)
        }.sortedByDescending { it.score }
    }

    fun updatePage(page: Int) {
        _uiState.update { currentState ->
            currentState.copy(currentPage = page)
        }
    }
}