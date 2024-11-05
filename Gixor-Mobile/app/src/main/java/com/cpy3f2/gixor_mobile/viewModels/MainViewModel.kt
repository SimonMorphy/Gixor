package com.cpy3f2.gixor_mobile.viewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.MyApplication


import com.cpy3f2.gixor_mobile.model.entity.Category
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import com.cpy3f2.gixor_mobile.network.source.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context
import androidx.lifecycle.LiveData
import com.cpy3f2.gixor_mobile.database.GixorDatabase
import com.cpy3f2.gixor_mobile.model.entity.Event
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import java.time.LocalDateTime
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import createQueryParams
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.Issue
import com.cpy3f2.gixor_mobile.model.entity.PullRequest
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import createPageQueryParams
import createStateQueryParams
import kotlinx.coroutines.coroutineScope
import android.util.Log
import com.cpy3f2.gixor_mobile.model.entity.Notification
import com.cpy3f2.gixor_mobile.model.setting.NotificationQuerySetting

class MainViewModel : ViewModel() {
    //热榜
    var hotList by mutableStateOf(
        listOf(
            "人物热榜",
            "项目热榜"
        )
    )
    // 更新 PagerState 的创建方式
    var categories by mutableStateOf(
        listOf(
            Category("动态"),
            Category("推荐"),
            Category("热点")
        )
    )

    var categoryIndex by mutableIntStateOf(0)
        private set

    val gitHubUser = MutableLiveData<ResultData<GitHubUser>>()
    fun getUserInfo(tokenValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                gitHubUser.postValue(RetrofitClient.httpBaseService.getMyUserInfo(tokenValue))
            }
        }
    }

    /***
     * 收藏 Star相关的
     */
    // 是否正在加载
    var isStarLoading by mutableStateOf(false)
    // 是否已收藏
    var isStarred by mutableStateOf(false)
        private set
    // 错误信息
    var starErrorMsg by mutableStateOf<String?>(null)
        private set

    /**
     * 更新分类下表
     * @param index
     */
    fun updateCategoriesIndex(index: Int){
        categoryIndex = index
    }


    /**
     *获取关注数据
     */
    //TODO 获取网络请求中的数据

    /**
     * 获取关注对象的动态
     * //TODO 获取网络请求中的数据
     */

    //从本地获取搜索数据

    private val searchHistoryDao = GixorDatabase.database.getSearchHistoryItemDao()

    // 使用 StateFlow 管理搜索历史
    private val _searchHistoryItems = MutableStateFlow<List<SearchHistoryItem>>(emptyList())
    val searchHistoryItems: StateFlow<List<SearchHistoryItem>> = _searchHistoryItems.asStateFlow()

    init {
        // 初始化时加载搜索历史
        viewModelScope.launch {
            loadSearchHistory()
        }
    }

    // 修改原来的 getSearchHistory 方法为 loadSearchHistory
    private suspend fun loadSearchHistory() {
        withContext(Dispatchers.IO) {
            _searchHistoryItems.value = searchHistoryDao.getAll()
        }
    }

    // 修改 insertSearchHistory 方法
    @SuppressLint("NewApi")
    fun addSearchHistory(searchText: String) {
        if (searchText.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            // 先查找是否存在相同的搜索记录
            val existingItem = searchHistoryDao.findByName(searchText)

            if (existingItem != null) {
                // 如果存在，只更新时间
                searchHistoryDao.updateTime(searchText, LocalDateTime.now())
            } else {
                // 如果不存在，创建新记录
                val searchItem = SearchHistoryItem(
                    id = 0, // Room会自动生成ID
                    name = searchText,
                    time = LocalDateTime.now()
                )
                searchHistoryDao.insert(searchItem)
            }

            // 重新加载搜索历史（已经按时间降序排序）
            loadSearchHistory()
        }
    }

    // 修改 deleteSearchHistory 方法
    fun deleteSearchHistory(item: SearchHistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryDao.delete(item)
            loadSearchHistory() // 重新加载搜索历史
        }
    }



    // 检查是否有token
    fun hasToken(): Boolean = preferencesManager.hasToken()

    // 导航方法
    fun navigateToLogin() {
        NavigationManager.navigateToLogin()
    }

    fun navigateToMain() {
        NavigationManager.navigateToMain()
    }

    fun navigateToSearch() {
        NavigationManager.navigateToSearch()
    }

    // 检查登录状态
    fun checkLoginStatus() {
        val sharedPreferences = MyApplication.getApplicationContext()
            .getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token == null) {
            navigateToLogin()
        }
    }



    // 清空所有搜索历史
    fun clearSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryDao.clearAll()
            loadSearchHistory()
        }
    }

    private val preferencesManager = MyApplication.preferencesManager

    private val _isLoggedIn = MutableStateFlow(preferencesManager.hasToken())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // 添加登录状态流
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // 定义登录状态
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun handleLoginSuccess(jsonResponse: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // 保存完整的响应
                preferencesManager.saveToken(jsonResponse)

                // 更新登录状态
                _isLoggedIn.value = true
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                e.printStackTrace()
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
                // 登录失败时清除token
                preferencesManager.clearToken()
                _isLoggedIn.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearToken()
            _isLoggedIn.value = false
        }
    }

    fun getToken(): String? = preferencesManager.getToken()

    private val _trendyRepos = MutableStateFlow<List<TrendyRepository>>(emptyList())
    val trendyRepos: StateFlow<List<TrendyRepository>> = _trendyRepos.asStateFlow()

    // 添加一个标志来追踪是否已经加载过数据
    private var _hasLoadedTrendyRepos = false

    fun loadTrendyRepos() {
        // 如果已经加载过且有数据，就不重复加载
        if (_hasLoadedTrendyRepos && _trendyRepos.value.isNotEmpty()) return
        
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val response = getToken()?.let { RetrofitClient.httpBaseService.getTrendyRepoList(it) }
                if (response != null) {
                    if (response.code == 200) {
                        _trendyRepos.value = response.data!!
                        _hasLoadedTrendyRepos = true
                        
                        if (hasToken()) {
                            checkStarStatusForRepos(response.data)
                        }
                    }
                }
                _uiState.value = UiState.Idle
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error(e.message ?: "加载失败")
            }
        }
    }

    private fun checkStarStatusForRepos(repos: List<TrendyRepository>) {
        if (!hasToken()) return

        viewModelScope.launch {
            try {
                val token = getToken() ?: return@launch
                // 对每个仓库单独检查收藏状态
                repos.forEach { repo ->
                    try {
                        val response = RetrofitClient.httpBaseService.isStarRepo(repo.author, repo.name, token)
                        if (response.code == 200) {
                            // 如果仓库被收藏，将其ID添加到收藏集合中
                            _starredRepos.value = _starredRepos.value + "${repo.author}/${repo.name}"
                        }
                    } catch (e: Exception) {
                        // 单个仓库检查失败影响其他仓库
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 存储仓库的收藏状态
    private val _starredRepos = MutableStateFlow<Set<String>>(emptySet())
    val starredRepos: StateFlow<Set<String>> = _starredRepos.asStateFlow()

    // 加载用户的收藏仓库列表
    fun loadStarredRepos() {
        if (!hasToken()) return

        viewModelScope.launch {
            try {
                createQueryParams()
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getMyStarRepoList(createQueryParams(),token)
                if (response.code == 200) {
                    val starredRepoIds = response.data?.map { "${it.owner?.name}/${it.name}" }
                        ?.toSet()
                    if (starredRepoIds != null) {
                        _starredRepos.value = starredRepoIds
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Star/Unstar 仓库
    fun toggleStarRepo(owner: String, name: String, isCurrentlyStarred: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val token = getToken() ?: return@launch
                val repoId = "$owner/$name"

                val success = if (isCurrentlyStarred) {
                    // 取消收藏
                    val response = RetrofitClient.httpBaseService.unStarRepo(owner, name, token)
                    if (response.code == 200) {
                        _starredRepos.value = _starredRepos.value - repoId
                        true
                    } else false
                } else {
                    // 添加收藏
                    val response = RetrofitClient.httpBaseService.starRepo(owner, name, token)
                    if (response.code == 200) {
                        _starredRepos.value = _starredRepos.value + repoId
                        true
                    } else false
                }

                if (success) {
                    _uiState.value = UiState.Success
                } else {
                    _uiState.value = UiState.Error("操作失败")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error(e.message ?: "操作失败")
                if (e.message?.contains("Token") == true) {
                    _isLoggedIn.value = false
                    preferencesManager.clearToken()
                }
            }
        }
    }

    init {
        // 在初始化时加载收藏列表
        loadStarredRepos()
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 如果需要检查单个仓库的收藏状态，可以使用这个扩展方法
    fun isRepoStarred(owner: String, name: String): Boolean {
        return "$owner/$name" in _starredRepos.value
    }

    private val _repoDetails = MutableStateFlow<GitHubRepository?>(null)
    val repoDetails: StateFlow<GitHubRepository?> = _repoDetails.asStateFlow()

    // 添加加载状态
    private val _isRepoDetailsLoading = MutableStateFlow(false)
    val isRepoDetailsLoading: StateFlow<Boolean> = _isRepoDetailsLoading.asStateFlow()

    fun loadRepoDetails(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                _isRepoDetailsLoading.value = true  // 开始加载
                val params = createQueryParams()
                val response = getToken()?.let { RetrofitClient.httpBaseService.getUserRepoList(it, owner, params) }
                if (response != null) {
                    if (response.code == 200) {
                        val repo = response.data?.find { it.name == repoName }
                        _repoDetails.value = repo
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isRepoDetailsLoading.value = false  // 结束加载
            }
        }
    }

    //issue
    private val _repoIssues = MutableStateFlow<List<Issue>>(emptyList())
    val repoIssues: StateFlow<List<Issue>> = _repoIssues.asStateFlow()

    private val _isIssuesLoading = MutableStateFlow(false)
    val isIssuesLoading: StateFlow<Boolean> = _isIssuesLoading.asStateFlow()

    // 添加状态跟踪
    var selectedFilter by mutableStateOf("open")
        private set

    // 添加更新过滤器状态的方法
    fun updateIssueFilter(filter: String) {
        selectedFilter = filter
    }

    fun loadRepoIssues(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                _isIssuesLoading.value = true  // 开始加载
                val params = createStateQueryParams(state = selectedFilter) // 使用选中的过滤器状态
                val response = getToken()?.let { 
                    RetrofitClient.httpBaseService.getRepoIssues(it, owner, repoName, params)
                }
                if (response != null) {
                    if (response.code == 200) {
                        _repoIssues.value = response.data!!
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isIssuesLoading.value = false  // 结束加载
            }
        }
    }


    //pr
    private val _repoPullRequests = MutableStateFlow<List<PullRequest>>(emptyList())
    val repoPullRequests: StateFlow<List<PullRequest>> = _repoPullRequests.asStateFlow()

    private val _isPrLoading = MutableStateFlow(false)
    val isPrLoading: StateFlow<Boolean> = _isPrLoading.asStateFlow()

    // Add PR filter state tracking
    var selectedPrFilter by mutableStateOf("all")
        private set

    // Add method to update PR filter
    fun updatePrFilter(filter: String) {
        selectedPrFilter = filter
    }

    fun loadRepoPullRequests(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                _isPrLoading.value = true
                val response = getToken()?.let { 
                    RetrofitClient.httpBaseService.getRepoPrList(it, owner, repoName, createStateQueryParams(state = selectedPrFilter))
                }
                if (response != null) {
                    if (response.code == 200) {
                        _repoPullRequests.value = response.data!!
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPrLoading.value = false
            }
        }
    }

    private val _currentIssue = MutableStateFlow<Issue?>(null)
    val currentIssue: StateFlow<Issue?> = _currentIssue.asStateFlow()

    private val _isIssueDetailLoading = MutableStateFlow(false)
    val isIssueDetailLoading: StateFlow<Boolean> = _isIssueDetailLoading.asStateFlow()

    fun loadIssueDetail(owner: String, repo: String, issueNumber: Long) {
        viewModelScope.launch {
            try {
                _isIssueDetailLoading.value = true
                val response = getToken()?.let { 
                    RetrofitClient.httpBaseService.getIssueDetail(owner, repo, issueNumber)
                }
                if (response?.code == 200) {
                    _currentIssue.value = response.data as Issue
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isIssueDetailLoading.value = false
            }
        }
    }

    private val _currentPullRequest = MutableStateFlow<PullRequest?>(null)
    val currentPullRequest: StateFlow<PullRequest?> = _currentPullRequest.asStateFlow()

    private val _isPrDetailLoading = MutableStateFlow(false)
    val isPrDetailLoading: StateFlow<Boolean> = _isPrDetailLoading.asStateFlow()

    fun loadPullRequestDetail(owner: String, repo: String, prNumber: Long) {
        viewModelScope.launch {
            try {
                _isPrDetailLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getPrDetail(token, owner, repo, prNumber)
                if (response.code == 200) {
                    _currentPullRequest.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPrDetailLoading.value = false
            }
        }
    }

    // 添加预加载状态
    private val _isPreloading = MutableStateFlow(false)
    val isPreloading: StateFlow<Boolean> = _isPreloading.asStateFlow()

    // 预加载数据方法
    suspend fun preloadData() {
        if (_isPreloading.value) return
        
        viewModelScope.launch {
            try {
                _isPreloading.value = true
                
                // 并行加载多个数据
                coroutineScope {
                    launch { loadTrendyRepos() }
                    
                    // 如果用户已登录，加载用户相关数据
                    if (hasToken()) {
                        launch { 
                            getToken()?.let { token ->
                                getUserInfo(token)
                            }
                        }
                        launch { loadStarredRepos() }
                    }
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPreloading.value = false
            }
        }
    }

    // 添加关注者列表状态
    private val _followingList = MutableStateFlow<List<SimpleUser>>(emptyList())
    val followingList: StateFlow<List<SimpleUser>> = _followingList.asStateFlow()

    // 添加加载状态
    private val _isFollowingLoading = MutableStateFlow(false)
    val isFollowingLoading: StateFlow<Boolean> = _isFollowingLoading.asStateFlow()

    // 获取关注者列表
    fun loadFollowingList() {
        viewModelScope.launch {
            try {
                _isFollowingLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getMyFollowing(token)
                if (response.code == 200) {
                    _followingList.value = response.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isFollowingLoading.value = false
            }
        }
    }

    // 添加公共动态列表状态
    private val _publicEvents = MutableStateFlow<List<Event>>(emptyList())
    val publicEvents: StateFlow<List<Event>> = _publicEvents.asStateFlow()

    private val _isEventsLoading = MutableStateFlow(false)
    val isEventsLoading: StateFlow<Boolean> = _isEventsLoading.asStateFlow()

    // 获取公共动态
    fun loadPublicEvents() {
        viewModelScope.launch {
            try {
                _isEventsLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getPublicEvent(token, createPageQueryParams())
                if (response.code == 200) {
                    _publicEvents.value = response.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isEventsLoading.value = false
            }
        }
    }

    // 添加用户相关动态状态
    private val _relatedEvents = MutableStateFlow<List<Event>>(emptyList())
    val relatedEvents: StateFlow<List<Event>> = _relatedEvents.asStateFlow()

    private val _isRelatedEventsLoading = MutableStateFlow(false)
    val isRelatedEventsLoading: StateFlow<Boolean> = _isRelatedEventsLoading.asStateFlow()

    // 加载用户相关动态
    fun loadRelatedEvents(username: String) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Starting to load related events for: $username")
                _isRelatedEventsLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getRelatedEvent(token, username)
                Log.d("MainViewModel", "Related events response: ${response.code}")
                if (response.code == 200) {
                    val events = response.data ?: emptyList()
                    Log.d("MainViewModel", "Received ${events.size} related events")
                    _relatedEvents.value = events
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading related events", e)
            } finally {
                _isRelatedEventsLoading.value = false
            }
        }
    }

    // 添加接收到的动态列表状态
    private val _receivedEvents = MutableStateFlow<List<Event>>(emptyList())
    val receivedEvents: StateFlow<List<Event>> = _receivedEvents.asStateFlow()

    private val _isReceivedEventsLoading = MutableStateFlow(false)
    val isReceivedEventsLoading: StateFlow<Boolean> = _isReceivedEventsLoading.asStateFlow()

    // 加载接收到的动态
    fun loadReceivedEvents(username: String) {
        viewModelScope.launch {
            try {
                _isReceivedEventsLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getReceivedEvent(token, username)
                if (response.code == 200) {
                    _receivedEvents.value = response.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isReceivedEventsLoading.value = false
            }
        }
    }

    // 添加通知列表状态
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _isNotificationsLoading = MutableStateFlow(false)
    val isNotificationsLoading: StateFlow<Boolean> = _isNotificationsLoading.asStateFlow()

    // 添加分页状态
    private var currentPage = 1
    private var hasMoreNotifications = true

    private val _notificationError = MutableStateFlow<String?>(null)
    val notificationError: StateFlow<String?> = _notificationError.asStateFlow()

    // 修改通知加载逻辑
    fun loadNotifications(isRefresh: Boolean = false) {
        if (_isNotificationsLoading.value) return
        if (isRefresh) {
            currentPage = 1
            hasMoreNotifications = true
            _notificationError.value = null
        }
        if (!hasMoreNotifications) return

        viewModelScope.launch {
            try {
                _isNotificationsLoading.value = true
                val token = getToken() ?: return@launch

                // 创建查询设置，优先获取未读消息并按未读数量排序
                val querySettings = NotificationQuerySetting(
                    perPage = 10,
                    page = currentPage,
                    isUnread = true,  // 优先获取未读消息
                    sort = "updated",  // 按更新时间排序
                    direction = "desc"  // 降序排列
                )

                // 获取未读消息
                val unreadResponse = RetrofitClient.httpBaseService.getNotificationList(token, querySettings.toQueryMap())
                
                if (unreadResponse.code == 200) {
                    val unreadNotifications = unreadResponse.data ?: emptyList()
                    
                    // 如果未读消息不足10条，获取已读消息补充
                    if (unreadNotifications.size < 10) {
                        val remainingCount = 10 - unreadNotifications.size
                        val readQuerySettings = NotificationQuerySetting(
                            perPage = remainingCount,
                            page = 1,
                            isUnread = false,
                            sort = "updated",
                            direction = "desc"
                        )
                        
                        val readResponse = RetrofitClient.httpBaseService.getNotificationList(token, readQuerySettings.toQueryMap())
                        
                        if (readResponse.code == 200) {
                            val readNotifications = readResponse.data ?: emptyList()
                            
                            // 合并未读和已读消息
                            _notifications.value = if (isRefresh) {
                                unreadNotifications + readNotifications
                            } else {
                                _notifications.value + unreadNotifications + readNotifications
                            }
                        }
                    } else {
                        // 如果未读消息足够，直接使用未读消息
                        _notifications.value = if (isRefresh) {
                            unreadNotifications
                        } else {
                            _notifications.value + unreadNotifications
                        }
                    }

                    // 更新分页状态
                    hasMoreNotifications = unreadNotifications.size == 10
                    if (hasMoreNotifications) currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _notificationError.value = e.message ?: "加载失败"
            } finally {
                _isNotificationsLoading.value = false
            }
        }
    }

    // 添加刷新方法
    fun refreshNotifications() {
        loadNotifications(isRefresh = true)
    }
}