package com.cpy3f2.gixor_mobile.viewModel

import GitHubUser
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cpy3f2.gixor_mobile.database.GixorDatabase

import com.cpy3f2.gixor_mobile.model.entity.Category
import com.cpy3f2.gixor_mobile.model.entity.FocusContentItem
import com.cpy3f2.gixor_mobile.model.entity.FocusItem
import com.cpy3f2.gixor_mobile.model.entity.ResultData
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import com.cpy3f2.gixor_mobile.model.entity.Token
import com.cpy3f2.gixor_mobile.utils.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

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

    //登录模块
    var loginData = MutableLiveData<ResultData<Token>>()
    fun login(username: String, password: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                loginData.postValue(RetrofitClient.httpBaseService.login(username,password))
            }
        }
    }
    val gitHubUser = MutableLiveData<ResultData<GitHubUser>>()
    fun getUserInfo(token: Token) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                gitHubUser.postValue(RetrofitClient.httpBaseService.getGitHubUserInfo(token.tokenValue))
            }
        }
    }

    /**
     * 更新分类下表
     * @param index
     */
    fun updateCategoriesIndex(index: Int){
        categoryIndex = index
    }

//    val httpData : MutableLiveData<User>()
//    val doLoginRepostory by lazy {
//        LoginRepostory()
//    }

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

    // 添加数据库相关的变量
    var searchHistoryItems by mutableStateOf<List<SearchHistoryItem>>(emptyList())
    
    // 添加获取搜索历史的方法
    fun getSearchHistory(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchHistoryItems = GixorDatabase
                    .database
                    .getSearchHistoryItemDao()
                    .getAll()
            }
        }
    }

    // 添加插入搜索历史的方法
    fun insertSearchHistory(item: SearchHistoryItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                GixorDatabase
                    .database
                    .getSearchHistoryItemDao()
                    .insert(item)
                // 重新加载数据
                getSearchHistory()
            }
        }
    }

    // 添加删除搜索历史的方法
    fun deleteSearchHistory(item: SearchHistoryItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                GixorDatabase
                    .database
                    .getSearchHistoryItemDao()
                    .delete(item)
                // 重新加载数据
                getSearchHistory()
            }
        }
    }

}