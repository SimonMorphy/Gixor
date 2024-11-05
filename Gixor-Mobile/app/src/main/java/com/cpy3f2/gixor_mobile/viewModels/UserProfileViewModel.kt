package com.cpy3f2.gixor_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.MyApplication.Companion.preferencesManager
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.network.source.RetrofitClient
import createQueryParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _userState = MutableStateFlow(GitHubUser())
    val userState: StateFlow<GitHubUser> = _userState.asStateFlow()

    private val _repositories = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val repositories: StateFlow<List<GitHubRepository>> = _repositories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    fun getToken(): String? = preferencesManager.getToken()

    fun loadUserProfile(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val response =
                    getToken()?.let { RetrofitClient.httpBaseService.getGitHubUserInfo(it,username) }
                if (response != null) {
                    if (response.code == 200) {
                        _userState.value = response.data
                        loadUserRepositories(username)
                    } else {
                        _error.value = "Failed to load user profile"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadUserRepositories(username: String) {
        viewModelScope.launch {
            try {
                val response = getToken()?.let { RetrofitClient.httpBaseService.getUserRepoList(it,username, createQueryParams()) }
                if (response != null) {
                    if (response.code == 200) {
                        _repositories.value = response.data
                    } else {
                        _error.value = "Failed to load repositories"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
} 