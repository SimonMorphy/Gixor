package com.cpy3f2.gixor_mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.MyApplication
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.cpy3f2.gixor_mobile.model.setting.BaseQuerySetting
import com.cpy3f2.gixor_mobile.network.source.RetrofitClient
import com.cpy3f2.gixor_mobile.utils.EventBus
import com.cpy3f2.gixor_mobile.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager // 直接注入 PreferencesManager
) : ViewModel() {
    private val _isUserLoggedIn = MutableStateFlow(true)
    val isUserLoggedIn: StateFlow<Boolean> get() = _isUserLoggedIn


    private val _userProfile = MutableStateFlow<GitHubUser?>(null)
    val userProfile: StateFlow<GitHubUser?> = _userProfile

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 添加新的状态
    private val _followers = MutableStateFlow<List<SimpleUser>>(emptyList())
    val followers: StateFlow<List<SimpleUser>> = _followers

    private val _following = MutableStateFlow<List<SimpleUser>>(emptyList())
    val following: StateFlow<List<SimpleUser>> = _following

    private val _repositories = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val repositories: StateFlow<List<GitHubRepository>> = _repositories

    // 当前显示的内容类型
    private val _currentTab = MutableStateFlow("index")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 分页状态
    private var followersPage = 1
    private var followingPage = 1
    private var hasMoreFollowers = true
    private var hasMoreFollowing = true

    // 加载状态
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    // 添加仓库分页状态
    private var reposPage = 1
    private var hasMoreRepos = true

    // 添加用户仓库状态
    private val _userRepos = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val userRepos: StateFlow<List<GitHubRepository>> = _userRepos.asStateFlow()

    // 仓库分页状态
    private var userReposPage = 1
    private var hasMoreUserRepos = true

    fun logout() {
        viewModelScope.launch {
            // 清除 token
            preferencesManager.clearToken()
            // 设置未登录状态
            _isUserLoggedIn.value = false
            // 可以在这里添加导航到主页面等操作
            // NavigationManager.navigateToMain()
        }
    }

    init {

            loadUserProfile()
            viewModelScope.launch {
                EventBus.onFollowEvent().collect {
                    withContext(Dispatchers.IO) {
                        delay(1500) // 如果需要延迟，可以在 IO 线程中进行
                        loadUserProfile()
                    }
                }

        }

    }


    private fun getToken(): String? = MyApplication.preferencesManager.getToken()

    fun switchTab(tab: String, username: String) {
        if (_currentTab.value == tab) return

        _currentTab.value = tab
        when (tab) {
            "repositories" -> loadUserRepos(username, isRefresh = true)
            "followers" -> loadFollowers(username, isRefresh = true)
            "following" -> loadFollowing(username, isRefresh = true)
            "index" -> loadUserProfile()
        }
    }

    fun loadFollowers(username: String, isRefresh: Boolean = false) {
        if (_isLoadingMore.value) return
        if (isRefresh) {
            followersPage = 1
            hasMoreFollowers = true
            _followers.value = emptyList()
        }
        if (!hasMoreFollowers) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val token = getToken() ?: return@launch

                val querySettings = BaseQuerySetting(
                    perPage = 10, page = followersPage, sort = "created", direction = "desc"
                )

                val response = RetrofitClient.httpBaseService.getUserFollowers(
                    token, username, querySettings.toQueryMap()
                )

                if (response.code == 200) {
                    val newFollowers = response.data ?: emptyList()
                    _followers.value = if (isRefresh) {
                        newFollowers
                    } else {
                        _followers.value + newFollowers
                    }

                    hasMoreFollowers = newFollowers.size >= 10
                    if (hasMoreFollowers) followersPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // 加载关注者列表
    fun loadFollowing(username: String, isRefresh: Boolean = false) {
        if (_isLoadingMore.value) return
        if (isRefresh) {
            followingPage = 1
            hasMoreFollowing = true
            _following.value = emptyList()
        }
        if (!hasMoreFollowing) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val token = getToken() ?: return@launch

                val querySettings = BaseQuerySetting(
                    perPage = 10, page = followingPage, direction = "desc"
                )

                val response = RetrofitClient.httpBaseService.getUserFollowing(
                    token, username, querySettings.toQueryMap()
                )

                if (response.code == 200) {
                    val newFollowing = response.data ?: emptyList()
                    _following.value = if (isRefresh) {
                        newFollowing
                    } else {
                        _following.value + newFollowing
                    }

                    hasMoreFollowing = newFollowing.size >= 10
                    if (hasMoreFollowing) followingPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // 加载用户仓库
    fun loadUserRepos(username: String, isRefresh: Boolean = false) {
        if (_isLoadingMore.value) return
        if (isRefresh) {
            reposPage = 1
            hasMoreRepos = true
            _repositories.value = emptyList()
        }
        if (!hasMoreRepos) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val token = getToken() ?: return@launch

                val querySettings = BaseQuerySetting(
                    perPage = 5, page = reposPage, sort = "created", direction = "desc"
                )

                val response = RetrofitClient.httpBaseService.getUserRepoList(
                    token, username, querySettings.toQueryMap()
                )

                if (response.code == 200) {
                    val newRepos = response.data ?: emptyList()
                    _repositories.value = if (isRefresh) {
                        newRepos
                    } else {
                        _repositories.value + newRepos
                    }

                    hasMoreRepos = newRepos.size >= 5
                    if (hasMoreRepos) reposPage++
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    private val _userDetail = MutableStateFlow<GitHubUser?>(null)
    val userDetail: StateFlow<GitHubUser?> = _userDetail.asStateFlow()
    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken()
                token?.let {
                    _userProfile.value = fetchData(it)
                    _userDetail.value = fetchDetailData(it,userProfile.value?.login?:"")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchData(token: String): GitHubUser? {
        return try {
            RetrofitClient.httpBaseService.getMyUserInfo(token).data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private suspend fun fetchDetailData(token: String,username: String): GitHubUser? {
        return try {
            userProfile.value?.let {
                RetrofitClient.httpBaseService.getUserInfo(token,
                   username).data
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}