package com.cpy3f2.gixor_mobile.ui.screens

import GitHubRepoItem
import RecommendedRepoItem
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import kotlinx.coroutines.launch

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.IconButton

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.navigation.NavigationManager

import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, vm: MainViewModel = viewModel()) {
    val searchText by vm.searchText.collectAsState()
    val selectedTab by vm.selectedSearchTab.collectAsState()
    val tabs = listOf("人物热榜", "项目热榜")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        vm.updateSelectedSearchTab(pagerState.currentPage)
    }

    fun handleSearch() {
        if (searchText.isNotBlank()) {
            vm.addSearchHistory(searchText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // 搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.mipmap.back),
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    BasicTextField(
                        value = searchText,
                        onValueChange = { vm.updateSearchText(it) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardActions = KeyboardActions(
                            onSearch = { 
                                handleSearch()
                                vm.search(searchText)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        decorationBox = { innerTextField ->
                            Box {
                                if (searchText.isEmpty()) {
                                    Text(
                                        "搜索项目、用户、方向",
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "清除",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { vm.updateSearchText("") }
                        )
                    }
                }
            }
        }

        // 搜索历史
        if (searchText.isBlank()) {
            SearchHistoryComponent(
                vm = vm,
                onHistoryItemClick = { historyText ->
                    vm.updateSearchText(historyText)
                    handleSearch()
                    vm.search(historyText)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 标签栏和热榜内容
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { },
                indicator = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = if (selectedTab == index) 18.sp else 16.sp,
                                color = if (selectedTab == index) Color.Red else Color.Black,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // 热榜内容
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> PersonHotList(vm)
                    1 -> ProjectHotList(vm)
                }
            }
        } else {
            // 搜索结果标签栏
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { },
                indicator = { }
            ) {
                listOf("用户", "仓库").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = if (selectedTab == index) 18.sp else 16.sp,
                                color = if (selectedTab == index) Color.Red else Color.Black,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // 搜索结果内容
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                SearchResultsSection(searchText, page, vm)
            }
        }
    }
}

@Composable
private fun PersonHotList(viewModel: MainViewModel) {
    val trendyUsers by viewModel.trendyUsers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTrendyUsers()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(trendyUsers.size) { index ->
            val user = trendyUsers[index]
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { NavigationManager.navigateToUserProfile(user.username) }
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.titleLarge,
                        color = when (index) {
                            0 -> Color(0xFFFFD700) // 金
                            1 -> Color(0xFFC0C0C0) // 银
                            2 -> Color(0xFFCD7F32) // 铜
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectHotList(viewModel: MainViewModel) {
    val trendyRepos by viewModel.trendyRepos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTrendyRepos()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(trendyRepos.size) { index ->
            val repo = trendyRepos[index]
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { NavigationManager.navigateToRepoDetail(repo.author, repo.name) }
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.titleLarge,
                        color = when (index) {
                            0 -> Color(0xFFFFD700) // 金
                            1 -> Color(0xFFC0C0C0) // 银
                            2 -> Color(0xFFCD7F32) // 铜
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Column {
                        Text(
                            text = repo.author,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = repo.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        // 添加描述，如果有的话
                        if (!repo.description.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = repo.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchHistoryComponent(
    vm: MainViewModel,
    onHistoryItemClick: (String) -> Unit
) {
    val searchHistory by vm.searchHistoryItems.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        if (searchHistory.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "搜索历史",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 修改清除按钮部分
                Row(
                    modifier = Modifier
                        .clickable { vm.clearSearchHistory() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.mipmap.clear),
                        contentDescription = "清除历史",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "清除",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isExpanded)
                            Modifier.wrapContentHeight()
                        else
                            Modifier.height(72.dp)
                    )
                    .clip(RectangleShape)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    searchHistory.forEach { item ->
                        ChipItem(
                            text = item.name,
                            onClick = { onHistoryItemClick(item.name) }
                        )
                    }
                }
            }

            // 如果有超过两行的内容，显示展开/收起按钮
            // 假设每行平均显示3个项目
            if (searchHistory.size > 6) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) "收起" else "展开",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = if (isExpanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "收起" else "展开",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

//每一项的组件
@Composable
fun ChipItem(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFFF5F5F5),
        modifier = Modifier.height(32.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun SearchResultsSection(
    searchText: String,
    selectedTab: Int,
    vm: MainViewModel
) {
    val searchUserResults by vm.searchUserResults.collectAsState()
    val searchRepoResults by vm.searchRepoResults.collectAsState()
    val isSearching by vm.isSearching.collectAsState()
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val starredRepos by vm.starredRepos.collectAsState()

    if (searchText.isNotBlank()) {
        when (selectedTab) {
            0 -> { // 用户搜索结果
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                } else if (searchUserResults.isEmpty()) {
                    // 添加空结果提示
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "没有找到相关用户",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(searchUserResults.size) { index ->
                            val user = searchUserResults[index]
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        user.login?.let { username ->
                                            NavigationManager.navigateToUserProfile(username)
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = user.login ?: "未知用户",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            1 -> { // 仓库搜索结果
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                } else if (searchRepoResults.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "没有找到相关仓库",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(searchRepoResults) { repo ->
                            val repoId = "${repo.owner?.login}/${repo.name}"
                            val isStarred = starredRepos.contains(repoId)

                            GitHubRepoItem(
                                repo = repo,
                                onRepoClick = {
                                    repo.owner?.login?.let { owner ->
                                        NavigationManager.navigateToRepoDetail(owner, repo.name)
                                    }
                                },
                                onAuthorClick = {
                                    repo.owner?.login?.let { username ->
                                        NavigationManager.navigateToUserProfile(username)
                                    }
                                },
                                onLanguageClick = { /* 暂不实现 */ },
                                onStarClick = {
                                    if (isLoggedIn) {
                                        repo.owner?.login?.let { owner ->
                                            vm.toggleStarRepo(owner, repo.name, isStarred)
                                        }
                                    } else {
                                        vm.navigateToLogin()
                                    }
                                },
                                onLoginClick = {
                                    vm.navigateToLogin()
                                },
                                isStarred = isStarred,
                                isLoggedIn = isLoggedIn
                            )
                        }
                    }
                }
            }
        }
    }
}


