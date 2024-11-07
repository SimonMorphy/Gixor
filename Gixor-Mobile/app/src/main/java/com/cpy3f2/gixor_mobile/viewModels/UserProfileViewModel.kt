package com.cpy3f2.gixor_mobile.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.MyApplication.Companion.preferencesManager
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.cpy3f2.gixor_mobile.model.setting.BaseQuerySetting
import com.cpy3f2.gixor_mobile.network.source.RetrofitClient
import com.cpy3f2.gixor_mobile.utils.EventBus
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

    // 添加新的状态
    private val _followers = MutableStateFlow<List<SimpleUser>>(emptyList())
    val followers: StateFlow<List<SimpleUser>> = _followers.asStateFlow()

    private val _following = MutableStateFlow<List<SimpleUser>>(emptyList())
    val following: StateFlow<List<SimpleUser>> = _following.asStateFlow()

    // 当前显示的内容类型
    private val _currentTab = MutableStateFlow("repositories")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

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

    // 添加新的状态
    private val _watching = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val watching: StateFlow<List<GitHubRepository>> = _watching.asStateFlow()

    // 添加分页状态
    private var watchingPage = 1
    private var hasMoreWatching = true

    // 添加starred仓库相关状态
    private val _starredRepos = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val starredRepos: StateFlow<List<GitHubRepository>> = _starredRepos.asStateFlow()

    private var currentStarredPage = 1
    private var hasMoreStarred = true

    private fun getToken(): String? = preferencesManager.getToken()

    fun loadUserProfile(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _currentTab.value = "repositories"
                
                val token = getToken() ?: return@launch

                //TODO 后面注释掉第二行
//                val response = RetrofitClient.httpBaseService.getUserInfo(token, username)
                val response = RetrofitClient.httpBaseService.getGitHubUserInfo(token, username)
                if (response.code == 200) {
                    _userState.value = response.data!!
                    checkFollowingStatus(username)
                    loadUserRepos(username, isRefresh = true)
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
                    EventBus.emitFollowEvent()

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

    // 加载粉丝列表
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
                    perPage = 10,
                    page = followersPage,
                    sort = "created",
                    direction = "desc"
                )
                
                val response = RetrofitClient.httpBaseService.getUserFollowers(token, username, querySettings.toQueryMap())
                
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
                    perPage = 10,
                    page = followingPage,
                    direction = "desc"
                )
                
                val response = RetrofitClient.httpBaseService.getUserFollowing(token, username, querySettings.toQueryMap())
                
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
                    perPage = 5,
                    page = reposPage,
                    sort = "created",
                    direction = "desc"
                )
                
                val response = RetrofitClient.httpBaseService.getUserRepoList(
                    token,
                    username,
                    querySettings.toQueryMap()
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

    // 清理函数 - 在 ViewModel 被清理时重置状态
    override fun onCleared() {
        super.onCleared()
        initialFollowingState = false
    }

    // 切换标签并加载数据
    fun switchTab(tab: String, username: String) {
        if (_currentTab.value == tab) return
        
        _currentTab.value = tab
        when (tab) {
            "repositories" -> loadUserRepos(username, isRefresh = true)
            "followers" -> loadFollowers(username, isRefresh = true)
            "following" -> loadFollowing(username, isRefresh = true)
            "watching" -> loadWatching(username, isRefresh = true)
            "starred" -> loadStarredRepos(username, isRefresh = true)
        }
    }

    // 加载用户关注的仓库列表
    fun loadWatching(username: String, isRefresh: Boolean = false) {
        if (_isLoadingMore.value) return
        if (isRefresh) {
            watchingPage = 1
            hasMoreWatching = true
            _watching.value = emptyList()
        }
        if (!hasMoreWatching) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val token = getToken() ?: return@launch
                
                val response = RetrofitClient.httpBaseService.getUserSubscribedList(token, username)
                
                if (response.code == 200) {
                    val newWatching = response.data ?: emptyList()
                    _watching.value = if (isRefresh) {
                        newWatching
                    } else {
                        _watching.value + newWatching
                    }
                    
                    hasMoreWatching = newWatching.size >= 10
                    if (hasMoreWatching) watchingPage++
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun loadStarredRepos(username: String, isRefresh: Boolean = false) {
        if (_isLoadingMore.value) return
        if (isRefresh) {
            currentStarredPage = 1
            hasMoreStarred = true
            _starredRepos.value = emptyList()
        }

        if (!hasMoreStarred) return

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                val token = preferencesManager.getToken() ?: return@launch

                val params = mapOf(
                    "page" to currentStarredPage.toString(),
                    "per_page" to "10"
                )

                val response = RetrofitClient.httpBaseService.getUserStarRepoList(
                    tokenValue = token,
                    username = username,
                    params = params
                )

                if (response.code == 200) {
                    val newRepos = response.data ?: emptyList()
                    if (newRepos.isEmpty()) {
                        hasMoreStarred = false
                    } else {
                        _starredRepos.value = _starredRepos.value + newRepos
                        currentStarredPage++
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "加载失败"
            } finally {
                _isLoadingMore.value = false
            }
        }
    }
} 