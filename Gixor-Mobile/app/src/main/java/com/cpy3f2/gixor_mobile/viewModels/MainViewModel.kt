package com.cpy3f2.gixor_mobile.viewModels

import CommentDTO
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
import com.cpy3f2.Gixor.Domain.DTO.ForkDTO
import com.cpy3f2.gixor_mobile.model.entity.Discussion
import com.cpy3f2.gixor_mobile.model.entity.IssueDTO
import com.cpy3f2.gixor_mobile.model.entity.Notification
import com.cpy3f2.gixor_mobile.model.setting.NotificationQuerySetting
import com.cpy3f2.gixor_mobile.model.entity.IssueComment
import com.cpy3f2.gixor_mobile.model.entity.TrendyUser
import com.cpy3f2.gixor_mobile.model.setting.BaseQuerySetting
import com.cpy3f2.gixor_mobile.model.setting.DiscussionQuerySetting

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


    //从本地获取搜索数据

    private val searchHistoryDao = GixorDatabase.database.getSearchHistoryItemDao()

    // 使用 StateFlow 管理搜索历史
    private val _searchHistoryItems = MutableStateFlow<List<SearchHistoryItem>>(emptyList())
    val searchHistoryItems: StateFlow<List<SearchHistoryItem>> = _searchHistoryItems.asStateFlow()

    init {
        // 初化加载搜索历史
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

    // 添加一个标志来是否已经加载过
    private var _hasLoadedTrendyRepos = false

    fun loadTrendyRepos() {
        // 如果已经加载过且有数据，就不重复加载
        if (_hasLoadedTrendyRepos && _trendyRepos.value.isNotEmpty()) return

        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val response = RetrofitClient.httpBaseService.getTrendyRepoList()
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
                // 对每个仓库单独检查藏状态
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
                    val starredRepoIds = response.data?.map { "${it.owner?.login}/${it.name}" }
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
                    // 添加收
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

    // 如果需要检查单个仓库的收藏状态，以使用这个扩展方法
    fun isRepoStarred(owner: String, name: String): Boolean {
        return "$owner/$name" in _starredRepos.value
    }

    // 仓库详情相关状态
    private val _repoDetails = MutableStateFlow<GitHubRepository?>(null)
    val repoDetails: StateFlow<GitHubRepository?> = _repoDetails.asStateFlow()

    private val _isRepoDetailsLoading = MutableStateFlow(false)
    val isRepoDetailsLoading: StateFlow<Boolean> = _isRepoDetailsLoading.asStateFlow()

    private val _repoDetailsError = MutableStateFlow<String?>(null)
    val repoDetailsError: StateFlow<String?> = _repoDetailsError.asStateFlow()

    fun loadRepoDetails(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                _isRepoDetailsLoading.value = true
                _repoDetailsError.value = null

                val token = getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.getRepoDetail(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    params = createPageQueryParams()
                )

                if (response.code == 200) {
                    _repoDetails.value = response.data
                    // 如果用户已登录，检查 star 态
                    if (hasToken()) {
                        checkStarStatusForRepo(owner, repo)
                    }
                } else {
                    _repoDetailsError.value = response.msg ?: "Failed to load repository details"
                }
            } catch (e: Exception) {
                _repoDetailsError.value = e.message ?: "Unknown error"
            } finally {
                _isRepoDetailsLoading.value = false
            }
        }
    }

    // 检查单个仓库的 star 状态
    private suspend fun checkStarStatusForRepo(owner: String, repo: String) {
        try {
            val token = getToken() ?: return
            val response = RetrofitClient.httpBaseService.isStarRepo(owner, repo, token)
            if (response.code == 200) {
                _starredRepos.value = _starredRepos.value + "$owner/$repo"
            }
        } catch (e: Exception) {
            // 如果检查失败，假设未 star
            e.printStackTrace()
        }
    }

    //issue
    private val _repoIssues = MutableStateFlow<List<Issue>>(emptyList())
    val repoIssues: StateFlow<List<Issue>> = _repoIssues.asStateFlow()

    private val _isIssuesLoading = MutableStateFlow(false)
    val isIssuesLoading: StateFlow<Boolean> = _isIssuesLoading.asStateFlow()

    // 态跟
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

    // 加载数据方法
    suspend fun preloadData() {
        if (_isPreloading.value) return

        viewModelScope.launch {
            try {
                _isPreloading.value = true

                // 并行加载多个数据
                coroutineScope {
                    launch { loadTrendyRepos() }

                    // 如果用户已登录，加载用户相关据
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
                val response = RetrofitClient.httpBaseService.getPublicEvent(createPageQueryParams())
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

    // Issue 创建相关状态
    private val _isIssueCreating = MutableStateFlow(false)
    val isIssueCreating: StateFlow<Boolean> = _isIssueCreating.asStateFlow()

    private val _issueCreationError = MutableStateFlow<String?>(null)
    val issueCreationError: StateFlow<String?> = _issueCreationError.asStateFlow()

    fun createIssue(
        owner: String,
        repo: String,
        issue: IssueDTO
    ) {
        viewModelScope.launch {
            try {
                _isIssueCreating.value = true
                _issueCreationError.value = null

                val token = getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.createIssue(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    body = issue
                )

                if (response.code == 200) {
                    // 刷新 issues 列表
                    loadRepoIssues(owner, repo)
                    // 创建成功后清除错误状态
                    _issueCreationError.value = null
                } else {
                    throw Exception(response.msg ?: "Failed to create issue")
                }
            } catch (e: Exception) {
                _issueCreationError.value = e.message ?: "Unknown error occurred"
            } finally {
                _isIssueCreating.value = false
            }
        }
    }

    // 评论列表状态
    private val _issueComments = MutableStateFlow<List<IssueComment>>(emptyList())
    val issueComments: StateFlow<List<IssueComment>> = _issueComments.asStateFlow()

    private val _isCommentsLoading = MutableStateFlow(false)
    val isCommentsLoading: StateFlow<Boolean> = _isCommentsLoading.asStateFlow()

    // 加载评论列表
    fun loadIssueComments(owner: String, repo: String, issueNumber: Long) {
        viewModelScope.launch {
            try {
                _isCommentsLoading.value = true
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.getIssueComments(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    number = issueNumber,
                    params = emptyMap()
                )
                if (response.code == 200) {
                    _issueComments.value = response.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isCommentsLoading.value = false
            }
        }
    }

    // 添加评论
    fun addComment(
        owner: String,
        repo: String,
        issueNumber: Long,
        comment: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.addComment(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    number = issueNumber,
                    body = comment
                )
                if (response.code == 200) {
                    // 重新加载所有评论和用户评论
                    loadIssueComments(owner, repo, issueNumber)
                    loadUserComments(owner, repo, issueNumber)
                    onSuccess()
                } else {
                    throw Exception(response.msg ?: "Failed to add comment")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }

    private val _isMarkingAllAsRead = MutableStateFlow(false)
    val isMarkingAllAsRead: StateFlow<Boolean> = _isMarkingAllAsRead

    fun markAllNotificationsAsRead() {

        viewModelScope.launch {
            _isMarkingAllAsRead.value = true
            try {
                // 调用一键已读接口
                val response = RetrofitClient.httpBaseService.getUnreadNotificationList(
                    tokenValue = preferencesManager.getToken() ?: "",
                    params = mapOf()
                )

                if (response.code == 200) {
                    // 刷新通知列表
                    loadNotifications(isRefresh = true)
                }
            } catch (e: Exception) {
                _notificationError.value = e.message ?: "标记已读失败"
            } finally {
                _isMarkingAllAsRead.value = false
            }
        }
    }

    private val _trendyUsers = MutableStateFlow<List<TrendyUser>>(emptyList())
    val trendyUsers: StateFlow<List<TrendyUser>> = _trendyUsers.asStateFlow()


    fun loadTrendyUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.httpBaseService.getHotUserList(
                    tokenValue = preferencesManager.getToken() ?: ""
                )
                if (response.code == 200) {
                    _trendyUsers.value = response.data ?: emptyList()
                }
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

    // 添加用户评论列表状态
    private val _userComments = MutableStateFlow<List<IssueComment>>(emptyList())
    val userComments: StateFlow<List<IssueComment>> = _userComments.asStateFlow()

    private val _isUserCommentsLoading = MutableStateFlow(false)
    val isUserCommentsLoading: StateFlow<Boolean> = _isUserCommentsLoading.asStateFlow()

    // 获取当前用户的评论列表
    fun loadUserComments(owner: String, repo: String, issueNumber: Long) {
        viewModelScope.launch {
            try {
                _isUserCommentsLoading.value = true
                val token = getToken() ?: return@launch

                // 创建查询参数，设置按创建时间排序
                val params = mapOf(
                    "sort" to "created",
                    "direction" to "desc"  // 可选：按降序排列，最新的评论在前
                )

                val response = RetrofitClient.httpBaseService.getIssueComments(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    number = issueNumber,
                    params = params
                )

                if (response.code == 200) {
                    // 过滤出前用户的评论
                    val currentUser = gitHubUser.value?.data?.login
                    _userComments.value = response.data?.filter {
                        it.user?.login == currentUser
                    } ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isUserCommentsLoading.value = false
            }
        }
    }

    // 更新评论
    fun updateComment(
        owner: String,
        repo: String,
        commentId: Long,
        body: String,
        issueNumber: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("Token not found")

                // 使用 CommentDTO 包装评论内容
                val commentDTO = CommentDTO(body = body)

                val response = RetrofitClient.httpBaseService.updateComment(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    commentId = commentId,
                    commentDTO = commentDTO  // 使用 DTO 对象
                )

                if (response.code == 200) {
                    loadIssueComments(owner, repo, issueNumber)
                    loadUserComments(owner, repo, issueNumber)
                    onSuccess()
                } else {
                    throw Exception(response.msg ?: "Failed to update comment")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }

    // 删除评论
    fun deleteComment(
        owner: String,
        repo: String,
        commentId: Long,
        issueNumber: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.deleteComment(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    commentId = commentId
                )

                if (response.code == 200) {
                    // 删除成功后重新加载评论列表
                    loadIssueComments(owner, repo, issueNumber)
                    loadUserComments(owner, repo, issueNumber)
                    onSuccess()
                } else {
                    throw Exception(response.msg ?: "Failed to delete comment")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateIssue(
        owner: String,
        repo: String,
        issueNumber: Long,
        title: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: throw Exception("Token not found")
                val issueDTO = IssueDTO(title = title)

                val response = RetrofitClient.httpBaseService.updateIssue(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    body = issueDTO
                )

                if (response.code == 200) {
                    loadIssueDetail(owner, repo, issueNumber)
                    onSuccess()
                } else {
                    throw Exception(response.msg ?: "Failed to update issue")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Fork 相关状态
    private val _isForkLoading = MutableStateFlow(false)
    val isForkLoading: StateFlow<Boolean> = _isForkLoading.asStateFlow()

    private val _forkError = MutableStateFlow<String?>(null)
    val forkError: StateFlow<String?> = _forkError.asStateFlow()

    // Fork仓库方法
    fun createFork(owner: String, repoName: String, forkDTO: ForkDTO) {
        viewModelScope.launch {
            try {
                _isForkLoading.value = true
                _forkError.value = null

                val token = getToken() ?: throw Exception("Token not found")

                // 使用 ForkDTO 创建请求
                val response = RetrofitClient.httpBaseService.forkRepo(
                    tokenValue = token,
                    owner = owner,
                    repo = repoName,
                    body = forkDTO
                )

                if (response.code == 200) {
                    // Fork成功后导航到新fork的仓库
                    NavigationManager.navigateToRepoDetail(
                        forkDTO.organization.ifEmpty {
                            gitHubUser.value?.data?.login ?: ""
                        },
                        repoName
                    )
                } else {
                    throw Exception(response.msg ?: "Failed to fork repository")
                }
            } catch (e: Exception) {
                _forkError.value = e.message ?: "Unknown error occurred"
            } finally {
                _isForkLoading.value = false
            }
        }
    }

    // Fork用户列表相关状态
    private val _forkUsers = MutableStateFlow<List<SimpleUser>>(emptyList())
    val forkUsers: StateFlow<List<SimpleUser>> = _forkUsers.asStateFlow()

    private val _isForkUsersLoading = MutableStateFlow(false)
    val isForkUsersLoading: StateFlow<Boolean> = _isForkUsersLoading.asStateFlow()

    private var currentForkPage = 1
    private var hasMoreForkUsers = true

    // 加载Fork用户列表
    fun loadForkUsers(owner: String, repo: String, isRefresh: Boolean = false) {
        if (isRefresh) {
            currentForkPage = 1
            hasMoreForkUsers = true
            _forkUsers.value = emptyList()
        }

        if (!hasMoreForkUsers || _isForkUsersLoading.value) return

        viewModelScope.launch {
            try {
                _isForkUsersLoading.value = true
                val token = getToken() ?: throw Exception("Token not found")

                val params = BaseQuerySetting(
                    perPage = 10,
                    page = currentForkPage
                ).toQueryMap()

                val response = RetrofitClient.httpBaseService.getRepoForkUserList(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    params = params
                )

                if (response.code == 200) {
                    val newUsers = response.data ?: emptyList()
                    if (newUsers.isEmpty()) {
                        hasMoreForkUsers = false
                    } else {
                        _forkUsers.value = _forkUsers.value + newUsers
                        currentForkPage++
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isForkUsersLoading.value = false
            }
        }
    }

    // Stars 相关状
    private val _starUsers = MutableStateFlow<List<SimpleUser>>(emptyList())
    val starUsers: StateFlow<List<SimpleUser>> = _starUsers.asStateFlow()

    private val _isStarUsersLoading = MutableStateFlow(false)
    val isStarUsersLoading: StateFlow<Boolean> = _isStarUsersLoading.asStateFlow()

    private var currentStarPage = 1
    private var hasMoreStarUsers = true

    fun loadStarUsers(owner: String, repoName: String, isRefresh: Boolean = false) {
        if (_isStarUsersLoading.value) return
        if (isRefresh) {
            currentStarPage = 1
            hasMoreStarUsers = true
            _starUsers.value = emptyList()
        }
        if (!hasMoreStarUsers) return

        viewModelScope.launch {
            try {
                _isStarUsersLoading.value = true
                val token = getToken() ?: return@launch

                val params = createPageQueryParams(
                    page = currentStarPage,
                    perPage = 10
                )

                val response = RetrofitClient.httpBaseService.getSubscriberList(
                    tokenValue = token,
                    owner = owner,
                    repo = repoName,
                    params = params
                )

                if (response.code == 200) {
                    val newUsers = response.data ?: emptyList()
                    _starUsers.value = if (isRefresh) {
                        newUsers
                    } else {
                        _starUsers.value + newUsers
                    }

                    hasMoreStarUsers = newUsers.size == 10
                    if (hasMoreStarUsers) currentStarPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isStarUsersLoading.value = false
            }
        }
    }

    // 添加订阅状态
    private val _isSubscribed = MutableStateFlow<Set<String>>(emptySet())
    val isSubscribed: StateFlow<Set<String>> = _isSubscribed.asStateFlow()

    // 检查仓库是否被订阅
    fun checkRepoSubscription(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: return@launch
                val response = RetrofitClient.httpBaseService.isSubscribed(token, owner, repo)
                if (response.code == 200) {
                    _isSubscribed.value = _isSubscribed.value + "$owner/$repo"
                }
            } catch (e: Exception) {
                // 如果返回404或其他错误，说��未订阅
                _isSubscribed.value = _isSubscribed.value - "$owner/$repo"
            }
        }
    }

    // 切换订阅状态
    fun toggleSubscribeRepo(owner: String, repo: String, isCurrentlySubscribed: Boolean) {
        viewModelScope.launch {
            try {
                val token = getToken() ?: return@launch
                val response = if (isCurrentlySubscribed) {
                    RetrofitClient.httpBaseService.unsubscribeRepo(token, owner, repo)
                } else {
                    RetrofitClient.httpBaseService.subscribeRepo(token, owner, repo)
                }

                if (response.code == 200) {
                    if (isCurrentlySubscribed) {
                        _isSubscribed.value = _isSubscribed.value - "$owner/$repo"
                    } else {
                        _isSubscribed.value = _isSubscribed.value + "$owner/$repo"
                    }
                }
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

    fun lockIssue(
        owner: String,
        repo: String,
        issueNumber: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.lockIssue(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    number = issueNumber
                )

                if (response.code == 200) {
                    // 只更新 locked 状态，保持其他状态不变
                    _currentIssue.value = _currentIssue.value?.copy(locked = true)
                    onSuccess()
                } else {
                    onError(response.msg ?: "Failed to lock issue")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun unlockIssue(
        owner: String,
        repo: String,
        issueNumber: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken() ?: throw Exception("Token not found")
                val response = RetrofitClient.httpBaseService.unlockIssue(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    number = issueNumber
                )

                if (response.code == 200) {
                    // 只更新 locked 状态，保持其他状态不变
                    _currentIssue.value = _currentIssue.value?.copy(locked = false)
                    onSuccess()
                } else {
                    onError(response.msg ?: "Failed to unlock issue")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    // 添加搜索结果状态
    private val _searchUserResults = MutableStateFlow<List<SimpleUser>>(emptyList())
    val searchUserResults: StateFlow<List<SimpleUser>> = _searchUserResults.asStateFlow()

    private val _searchRepoResults = MutableStateFlow<List<GitHubRepository>>(emptyList())
    val searchRepoResults: StateFlow<List<GitHubRepository>> = _searchRepoResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // 搜索方法
    fun search(query: String) {
        if (query.isBlank()) {
            _searchUserResults.value = emptyList()
            _searchRepoResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isSearching.value = true
                
                // 并行执行搜索
                coroutineScope {
                    launch {
                        val userResponse = RetrofitClient.httpBaseService.searchUser(query)
                        if (userResponse.code == 200) {
                            // 添加空值检查
                            _searchUserResults.value = userResponse.data?.items ?: emptyList()
                        }
                    }
                    
                    launch {
                        val repoResponse = RetrofitClient.httpBaseService.searchRepo(query)
                        if (repoResponse.code == 200) {
                            // 添加空值检查
                            _searchRepoResults.value = repoResponse.data?.repositories ?: emptyList()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 发生错误时清空结果
                _searchUserResults.value = emptyList()
                _searchRepoResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    // 添加搜索状态相关变量
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _selectedSearchTab = MutableStateFlow(0)
    val selectedSearchTab: StateFlow<Int> = _selectedSearchTab.asStateFlow()

    // 更新搜索文本
    fun updateSearchText(text: String) {
        _searchText.value = text
        if (text.isNotBlank()) {
            search(text)
        } else {
            _searchUserResults.value = emptyList()
            _searchRepoResults.value = emptyList()
        }
    }

    // 更新选中的标签页
    fun updateSelectedSearchTab(tab: Int) {
        _selectedSearchTab.value = tab
    }

    // 讨论列表状态
    private val _repoDiscussions = MutableStateFlow<List<Discussion>>(emptyList())
    val repoDiscussions: StateFlow<List<Discussion>> = _repoDiscussions.asStateFlow()

    private val _isDiscussionsLoading = MutableStateFlow(false)
    val isDiscussionsLoading: StateFlow<Boolean> = _isDiscussionsLoading.asStateFlow()

    // 添加分页相关状态
    private var _hasMoreDiscussions = MutableStateFlow(true)
    val hasMoreDiscussions: StateFlow<Boolean> = _hasMoreDiscussions.asStateFlow()

    private var currentDiscussionCursor: String = null.toString()

    // 修改讨论列表加载方法
    fun loadRepoDiscussions(owner: String, repo: String, isRefresh: Boolean = false) {
        if (_isDiscussionsLoading.value) return
        if (isRefresh) {
            currentDiscussionCursor = null.toString()
            _repoDiscussions.value = emptyList()
            _hasMoreDiscussions.value = true
        }
        if (!_hasMoreDiscussions.value) return

        viewModelScope.launch {
            try {
                _isDiscussionsLoading.value = true
                val token = getToken() ?: return@launch
                
                val querySettings = DiscussionQuerySetting.builder()
                    .first(10)  // 每页加载10条
                    .after(currentDiscussionCursor)
                    .build()
                
                val response = RetrofitClient.httpBaseService.getRepoDiscussionList(
                    tokenValue = token,
                    owner = owner,
                    repo = repo,
                    params = querySettings
                )

                if (response.code == 200) {
                    val discussionVO = response.data
                    if (discussionVO != null) {
                        // 更新讨论列表
                        _repoDiscussions.value = if (isRefresh) {
                            discussionVO.nodes
                        } else {
                            _repoDiscussions.value + discussionVO.nodes
                        }

                        // 更新分页状态
                        _hasMoreDiscussions.value = discussionVO.pageInfo.hasNextPage
                        currentDiscussionCursor = discussionVO.pageInfo.endCursor
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isDiscussionsLoading.value = false
            }
        }
    }

    // 添加刷新方法
    fun refreshDiscussions(owner: String, repo: String) {
        loadRepoDiscussions(owner, repo, isRefresh = true)
    }
}