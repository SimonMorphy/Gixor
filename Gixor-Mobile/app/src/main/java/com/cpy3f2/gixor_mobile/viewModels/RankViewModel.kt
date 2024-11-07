package com.cpy3f2.gixor_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.MyApplication
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.RankUiState
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.network.source.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RankUiState())
    val uiState: StateFlow<RankUiState> = _uiState.asStateFlow()
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    private val token = MyApplication.preferencesManager.getToken()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            token?.let {
                loadFilterOptions(it)
                loadUsers(it, isRefresh = true)
            }
        }
    }

    private suspend fun loadFilterOptions(token: String) {
        try {
            val nationsResponse = getNations(token)
            var nationalities = nationsResponse.data ?: listOf("USA", "China", "Japan", "Korea", "India")
            nationalities = listOf("All") + nationalities

            val domainsResponse = getDomains(token)
            var domains = domainsResponse.data ?: listOf("Android", "Web", "AI", "iOS")
            domains = listOf("All") + domains

            _uiState.update { it.copy(
                domains = domains,
                nationalities = nationalities
            )}
        } catch (e: Exception) {
            // Handle error
        }
    }

    private suspend fun loadUsers(token: String, isRefresh: Boolean) {
        try {
            _isLoadingMore.value = true
            val page = if (isRefresh) 1 else _uiState.value.currentPage

            val response = when {
                _uiState.value.selectedDomain != "All" && _uiState.value.selectedNationality != "All" ->
                    getRankByDomainAndNation(token, _uiState.value.selectedNationality,_uiState.value.selectedDomain,  page, PAGE_SIZE)
                _uiState.value.selectedDomain != "All" ->
                    getRankByDomain(token, _uiState.value.selectedDomain, page, PAGE_SIZE)
                _uiState.value.selectedNationality != "All" ->
                    getRankByNation(token, _uiState.value.selectedNationality, page, PAGE_SIZE)
                else ->
                    getRankList(token, page, PAGE_SIZE)
            }

            response.data?.let { newUsers ->
                _uiState.update { currentState ->
                    currentState.copy(
                        users = if (isRefresh) newUsers else currentState.users + newUsers,
                        filteredUsers = if (isRefresh) newUsers else currentState.filteredUsers + newUsers,
                        currentPage = page + 1,
                        hasMore = newUsers.size == PAGE_SIZE
                    )
                }
            }
        } finally {
            _isLoadingMore.value = false
        }
    }

    fun loadMoreData() {
        if (!_isLoadingMore.value && _uiState.value.hasMore) {
            viewModelScope.launch {
                token?.let { loadUsers(it, false) }
            }
        }
    }

    fun updateDomain(domain: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                selectedDomain = domain,
                currentPage = 1,
                hasMore = true
            )}
            token?.let { loadUsers(it, true) }
        }
    }

    fun updateNationality(nationality: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                selectedNationality = nationality,
                currentPage = 1,
                hasMore = true
            )}
            token?.let { loadUsers(it, true) }
        }
    }

    private suspend fun getRankList(token: String, page: Int, pageSize: Int): ResultData<List<GitHubUser>> {
        return try {
            RetrofitClient.httpBaseService.getRankList(token, page, pageSize)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    private suspend fun getNations(token: String): ResultData<List<String>> {
        return try {
            RetrofitClient.httpBaseService.getNationList(token)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    private suspend fun getDomains(token: String): ResultData<List<String>> {
        return try {
            RetrofitClient.httpBaseService.getDomainList(token)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    private suspend fun getRankByDomain(token: String, domain: String, page: Int, pageSize: Int): ResultData<List<GitHubUser>> {
        return try {
            RetrofitClient.httpBaseService.getDomainRankList(token, domain, page, pageSize)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    private suspend fun getRankByDomainAndNation(token: String, nation: String,domain: String, page: Int, pageSize: Int): ResultData<List<GitHubUser>> {
        return try {
            RetrofitClient.httpBaseService.getRankByDomainAndNation(token,nation,domain, page, pageSize)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    private suspend fun getRankByNation(token: String, nation: String, page: Int, pageSize: Int): ResultData<List<GitHubUser>> {
        return try {
            RetrofitClient.httpBaseService.getNationRankList(token, nation, page, pageSize)
        } catch (e: Exception) {
            ResultData(code = 500, msg = "网络异常")
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}