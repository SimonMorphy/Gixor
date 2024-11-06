package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.UserProfileViewModel
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.outlined.Code
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.outlined.Visibility

@Composable
fun UserProfileScreen(
    username: String,
    viewModel: UserProfileViewModel,
    modifier: Modifier = Modifier
) {
    val userState by viewModel.userState.collectAsState()
    val reposState by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val followers by viewModel.followers.collectAsState()
    val following by viewModel.following.collectAsState()
    val watching by viewModel.watching.collectAsState()

    LaunchedEffect(username) {
        viewModel.loadUserProfile(username)
    }

    LaunchedEffect(username) {
        viewModel.loadUserRepos(username, isRefresh = true)
        viewModel.switchTab("repositories", username)
    }

    Column(modifier = modifier.fillMaxSize()) {
        // 系统状态栏
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }

        // 主要内容
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (error != null) {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 固定的头部内容
                    ProfileHeader(userState, username, viewModel)
                    StatsBar(user = userState, viewModel = viewModel, username = username)

                    // 可滚动的内容区域
                    Box(modifier = Modifier.weight(1f)) {
                        when (currentTab) {
                            "repositories" -> RepositorySection(
                                reposState = reposState,
                                username = username,
                                viewModel = viewModel
                            )
                            "followers" -> UserList(
                                users = followers,
                                viewModel = viewModel,
                                username = username,
                                isFollowersList = true
                            )
                            "following" -> UserList(
                                users = following,
                                viewModel = viewModel,
                                username = username,
                                isFollowersList = false
                            )
                            "watching" -> WatchingSection(
                                watching = watching,
                                username = username,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: GitHubUser,
    username: String,
    viewModel: UserProfileViewModel
) {
    val isFollowing by viewModel.isFollowing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 头像
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 用名和昵称
        Column {
            Text(
                text = user.name ?: username,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = username,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Bio
        if (!user.bio.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.bio!!,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 位置信息
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = user.location ?: "No location",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 关注按钮
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { 
                if (isFollowing) {
                    viewModel.unfollowUser(username)
                } else {
                    viewModel.followUser(username)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
                contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
            ),
            border = if (isFollowing) {
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            } else null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = if (isFollowing) Icons.Outlined.People else Icons.Outlined.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isFollowing) "已关注" else "关注")
            }
        }

        // 显示错误信息
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // 网站链接
        if (!user.blog.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.blog,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* TODO: 打开网站链接 */ }
            )
        }
    }
}

@Composable
private fun StatsBar(
    user: GitHubUser,
    viewModel: UserProfileViewModel,
    username: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Outlined.People,
                value = user.followers.toString(),
                label = "followers",
                onClick = { viewModel.switchTab("followers", username) }
            )
            StatItem(
                icon = Icons.Outlined.PersonAdd,
                value = user.following.toString(),
                label = "following",
                onClick = { viewModel.switchTab("following", username) }
            )
            StatItem(
                icon = Icons.Outlined.Code,
                value = user.publicRepos.toString(),
                label = "repositories",
                onClick = { viewModel.switchTab("repositories", username) }
            )
            StatItem(
                icon = Icons.Outlined.Visibility,
                value = "watching",
                label = "watching",
                onClick = { viewModel.switchTab("watching", username) }
            )
        }
    }
}

@Composable
fun RepositorySection(
    reposState: List<GitHubRepository>,
    username: String,
    viewModel: UserProfileViewModel
) {
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrolledToEnd() }
            .collect { isEnd ->
                if (isEnd && !isLoadingMore) {
                    viewModel.loadUserRepos(username, isRefresh = false)
                }
            }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingMore),
        onRefresh = {
            viewModel.loadUserRepos(username, isRefresh = true)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(reposState.size) { index ->
                    reposState[index].name?.let {
                        RepositoryCard(
                            name = it,
                            description = reposState[index].description,
                            language = reposState[index].language,
                            stars = reposState[index].stargazersCount ?: 0,
                            onClick = {
                                NavigationManager.navigateToRepoDetail(username, it)
                            }
                        )
                        if (index < reposState.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            // 空状态显示
            if (reposState.isEmpty() && !isLoadingMore) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "暂无仓库",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepositoryCard(
    name: String,
    description: String?,
    language: String?,
    stars: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Public",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            if (!description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!language.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(language))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = language,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stars.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UserList(
    users: List<SimpleUser>,
    viewModel: UserProfileViewModel,
    username: String,
    isFollowersList: Boolean
) {
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrolledToEnd() }
            .collect { isEnd ->
                if (isEnd && !isLoadingMore) {
                    if (isFollowersList) {
                        viewModel.loadFollowers(username, isRefresh = false)
                    } else {
                        viewModel.loadFollowing(username, isRefresh = false)
                    }
                }
            }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingMore),
        onRefresh = {
            if (isFollowersList) {
                viewModel.loadFollowers(username, isRefresh = true)
            } else {
                viewModel.loadFollowing(username, isRefresh = true)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(users.size) { index ->
                    UserListItem(user = users[index])
                    if (index < users.size - 1) {
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            // 空状态显示
            if (users.isEmpty() && !isLoadingMore) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFollowersList) Icons.Outlined.People else Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isFollowersList) "暂无关注者" else "暂无关注",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 初始加载动画
            if (users.isEmpty() && isLoadingMore) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun UserListItem(user: SimpleUser) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { NavigationManager.navigateToUserProfile(user.login!!) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = user.login!!,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun getLanguageColor(language: String): Color {
    // 这里需要实现一个函数来根据语言返回相应的颜色
    // 你可以使用一个映射表来存储语言和颜色的对应关系
    // 或者使用一个算法来生成颜色
    return Color.Black
}

@Composable
private fun WatchingSection(
    watching: List<GitHubRepository>,
    username: String,
    viewModel: UserProfileViewModel
) {
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrolledToEnd() }
            .collect { isEnd ->
                if (isEnd && !isLoadingMore) {
                    viewModel.loadWatching(username, isRefresh = false)
                }
            }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoadingMore),
        onRefresh = {
            viewModel.loadWatching(username, isRefresh = true)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(watching.size) { index ->
                    RepositoryCard(
                        name = watching[index].name ?: "",
                        description = watching[index].description,
                        language = watching[index].language,
                        stars = watching[index].stargazersCount ?: 0,
                        onClick = {
                            watching[index].name?.let { repoName ->
                                NavigationManager.navigateToRepoDetail(watching[index].fullName.split("/")[0], repoName)
                            }
                        }
                    )
                    if (index < watching.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            // 空状态显示
            if (watching.isEmpty() && !isLoadingMore) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "暂无关注的仓库",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
} 