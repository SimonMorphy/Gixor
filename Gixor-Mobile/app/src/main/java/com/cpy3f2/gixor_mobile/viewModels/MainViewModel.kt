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
import com.cpy3f2.gixor_mobile.database.GixorDatabase

import com.cpy3f2.gixor_mobile.model.entity.Category
import com.cpy3f2.gixor_mobile.model.entity.FocusContentItem
import com.cpy3f2.gixor_mobile.model.entity.FocusItem
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
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import java.time.LocalDateTime
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import createQueryParams
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.Issue
import com.cpy3f2.gixor_mobile.model.entity.PullRequest

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
    var focusData  =  listOf(
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"),
        FocusItem("https://img13.360buyimg.com/n1/s450x450_jfs/t1/230553/4/14312/28419/65e97139F8d0b7587/fe216871f88cf51b.jpg","测试数据"))

    /**
     * 获取关注对象的动态
     * //TODO 获取网络请求中的数据
     */
    var focusContentData = listOf(
        FocusContentItem("https://img13.360buyimg.com/imagetools/jfs/t1/148456/39/13392/164681/605279caE5a940775/7c4d345f6b834795.jpg","关注1"),
    )
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

    init {
        loadTrendyRepos()
    }

    fun loadTrendyRepos() {
        viewModelScope.launch {
            try {
                val response = getToken()?.let { RetrofitClient.httpBaseService.getTrendyRepoList(it) }
                if (response != null) {
                    if (response.code == 200) {
                        // 先加载仓库列表（此时所有仓库默认未收藏）
                        _trendyRepos.value = response.data

                        // 如果用户已登录，异步检查每个仓库的收藏状态
                        if (hasToken()) {
                            checkStarStatusForRepos(response.data)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                        if (response.code == 200 && response.data == true) {
                            // 如果仓库被收藏，将其ID添加到收藏集合中
                            _starredRepos.value = _starredRepos.value + "${repo.author}/${repo.name}"
                        }
                    } catch (e: Exception) {
                        // 单个仓库检查失败不影响其他仓库
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
                    val starredRepoIds = response.data.map { "${it.owner?.name}/${it.name}" }.toSet()
                    _starredRepos.value = starredRepoIds
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

    fun loadRepoDetails(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                val params = createQueryParams()
                val response = getToken()?.let { RetrofitClient.httpBaseService.getUserRepoList(it,owner, params) }
                if (response != null) {
                    if (response.code == 200) {
                        // 找到匹配的仓库
                        val repo = response.data.find { it.name == repoName }
                        _repoDetails.value = repo
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //issue
    private val _repoIssues = MutableStateFlow<List<Issue>>(emptyList())
    val repoIssues: StateFlow<List<Issue>> = _repoIssues.asStateFlow()

    private val _isIssuesLoading = MutableStateFlow(false)
    val isIssuesLoading: StateFlow<Boolean> = _isIssuesLoading.asStateFlow()

    fun loadRepoIssues(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                _isIssuesLoading.value = true  // 开始加载
                val response = getToken()?.let { RetrofitClient.httpBaseService.getRepoIssues(it,owner, repoName, createQueryParams()) }
                if (response != null) {
                    if (response.code == 200) {
                        _repoIssues.value = response.data
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

    fun loadRepoPullRequests(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                _isPrLoading.value = true
                val response = getToken()?.let { RetrofitClient.httpBaseService.getRepoPrList(it,owner, repoName) }
                if (response != null) {
                    if (response.code == 200) {
                        _repoPullRequests.value = response.data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPrLoading.value = false
            }
        }
    }
}