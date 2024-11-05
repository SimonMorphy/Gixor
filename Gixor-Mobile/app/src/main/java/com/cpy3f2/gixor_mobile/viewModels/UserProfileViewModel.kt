package com.cpy3f2.gixor_mobile.viewModels

import android.util.Log
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

    private val _isFollowing = MutableStateFlow(false)
    val isFollowing: StateFlow<Boolean> = _isFollowing.asStateFlow()

    // 记录初始关注状态
    private var initialFollowingState = false

    fun getToken(): String? = preferencesManager.getToken()

    fun loadUserProfile(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _isFollowing.value = false
                initialFollowingState = false
                
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getGitHubUserInfo(token, username)
                if (response.code == 200) {
                    _userState.value = response.data!!
                    loadUserRepositories(username)
                    checkFollowingStatus(username)
                } else {
                    _error.value = "Failed to load user profile"
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
                        _repositories.value = response.data!!
                    } else {
                        _error.value = "Failed to load repositories"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    // 检查是否已关注
    private fun checkFollowingStatus(username: String) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.isFollowing(token, username)
                _isFollowing.value = response.code == 200
                initialFollowingState = _isFollowing.value
                Log.d("FollowStatus", "Initial following state: $initialFollowingState")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 关注用户
    fun followUser(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.followUser(token, username)
                if (response.code == 200) {
                    _isFollowing.value = true
                    // 如果是从未关注变为关注，更新计数
                    if (!initialFollowingState) {
                        _userState.value = _userState.value.copy(
                            followers = (_userState.value.followers ?: 0) + 1
                        )
                    }
                    // 更新初始状态为当前状态
                    initialFollowingState = true
                } else {
                    _error.value = "关注失败: ${response.msg}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "关注失败"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 取消关注
    fun unfollowUser(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.unfollowUser(token, username)
                if (response.code == 200) {
                    _isFollowing.value = false
                    // 如果是从已关注变为未关注，更新计数
                    if (initialFollowingState) {
                        _userState.value = _userState.value.copy(
                            followers = (_userState.value.followers ?: 0) - 1
                        )
                    }
                    // 更新初始状态为当前状态
                    initialFollowingState = false
                } else {
                    _error.value = "取消关注失败: ${response.msg}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "取消关注失败"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 清理函数 - 在 ViewModel 被清理时重置状态
    override fun onCleared() {
        super.onCleared()
        initialFollowingState = false
    }
} 