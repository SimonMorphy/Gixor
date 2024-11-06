package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository
import com.cpy3f2.gixor_mobile.model.entity.GitHubUser
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MineViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun MineScreen(viewModel: MineViewModel = hiltViewModel()) {
    val userProfile by viewModel.userProfile.collectAsState()
    val reposState by viewModel.repositories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val followers by viewModel.followers.collectAsState()
    val following by viewModel.following.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    // 监听滚动到底部
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrolledToEnd() }
            .collect { isEnd ->
                if (isEnd && !isLoadingMore && userProfile != null) {
                    when (currentTab) {
                        "repositories" -> viewModel.loadUserRepos(userProfile!!.login, false)
                        "followers" -> viewModel.loadFollowers(userProfile!!.login, false)
                        "following" -> viewModel.loadFollowing(userProfile!!.login, false)
                    }
                }
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (userProfile != null) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isLoadingMore),
            onRefresh = {
                when (currentTab) {
                    "repositories" -> viewModel.loadUserRepos(userProfile!!.login, true)
                    "followers" -> viewModel.loadFollowers(userProfile!!.login, true)
                    "following" -> viewModel.loadFollowing(userProfile!!.login, true)
                }
            }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 固定的头部内容
                item { ProfileHeader(userProfile!!) }
                item { Description(userProfile!!.bio) }
                item { UserStats(userProfile!!, viewModel, userProfile!!.login) }

                // 根据当前tab显示不同的内容
                when (currentTab) {
                    "index" -> {
                        item { ScoreContainer(userProfile!!) }
                    }
                    "repositories" -> {
                        items(reposState.size) { index ->
                            reposState[index].name?.let {
                                RepositoryCard(
                                    name = it,
                                    description = reposState[index].description,
                                    language = reposState[index].language,
                                    stars = reposState[index].stargazersCount ?: 0,
                                    onClick = {
                                        NavigationManager.navigateToRepoDetail(userProfile!!.login, it)
                                    }
                                )
                                if (index < reposState.size - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                    "followers" -> {
                        items(followers.size) { index ->
                            UserListItem(followers[index])
                            if (index < followers.size - 1) {
                                Divider()
                            }
                        }
                    }
                    "following" -> {
                        items(following.size) { index ->
                            UserListItem(following[index])
                            if (index < following.size - 1) {
                                Divider()
                            }
                        }
                    }
                }

                // 加载更多指示器
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
            if (currentTab != "index" &&
                ((currentTab == "repositories" && reposState.isEmpty()) ||
                        (currentTab == "followers" && followers.isEmpty()) ||
                        (currentTab == "following" && following.isEmpty())) &&
                !isLoadingMore
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(currentTab)
                }
            }
        }
    }
}
@Composable
private fun EmptyState(currentTab: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = when (currentTab) {
                "repositories" -> Icons.Outlined.Code
                "followers" -> Icons.Outlined.People
                "following" -> Icons.Outlined.PersonAdd
                else -> Icons.Outlined.Code
            },
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = when (currentTab) {
                "repositories" -> "暂无仓库"
                "followers" -> "暂无关注者"
                "following" -> "暂无关注"
                else -> ""
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
fun ProfileHeader(userProfile: GitHubUser) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = userProfile.avatarUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground), // 可选的占位符
            error = painterResource(id = R.drawable.ic_launcher_foreground) // 可选的错误图片
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = userProfile.name ?: "Unknown",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = userProfile.login ?: "Unknown",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Messages") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Messages")
                    },
                    onClick = {
                        // Handle message click
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sign out") },
                    leadingIcon = {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign out")
                    },
                    onClick = {
                        // Handle settings click
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun Description(description: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "个人简介",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = description ?: "No description available",
        )
    }
}


@Composable
fun UserStats(
    userProfile: GitHubUser,
    viewModel: MineViewModel,
    username: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        StatItem(
            icon = Icons.Outlined.Engineering,
            value = "",
            label = "index",
            onClick = { viewModel.switchTab("index", username) }
        )
        StatItem(
            icon = Icons.Outlined.People,
            value = userProfile.followers.toString(),
            label = "followers",
            onClick = { viewModel.switchTab("followers", username) }
        )
        StatItem(
            icon = Icons.Outlined.PersonAdd,
            value = userProfile.following.toString(),
            label = "following",
            onClick = { viewModel.switchTab("following", username) }
        )
        StatItem(
            icon = Icons.Outlined.Code,
            value = userProfile.publicRepos.toString(),
            label = "repositories",
            onClick = { viewModel.switchTab("repositories", username) }
        )
    }
}
@Composable
private fun StatItem(
    icon: ImageVector,
    value: String?,
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
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun ScoreContainer(userProfile: GitHubUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Developer Score",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Score", (userProfile.score ?: 0).toString())
                ScoreItem("Level", (userProfile.grade ?: 0).toString())
                ScoreItem("Stars", (userProfile.totalStars ?: 0).toString())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Issues", (userProfile.totalIssues ?: 0).toString())
                ScoreItem("Commits", (userProfile.totalCommits ?: 0).toString())
                ScoreItem("PRs", (userProfile.totalPRs ?: 0).toString())
            }
        }
    }
}

@Composable
fun ScoreItem(title: String, value: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value ?: "0",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = title, style = MaterialTheme.typography.bodySmall)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 标题行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "Public",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // 描述
                if (!description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // 只显示语言
                language?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Star 计数固定在右下角
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stars.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RepoStatItem(
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RepositorySection(
    reposState: List<GitHubRepository>,
    username: String,
    viewModel: MineViewModel
) {
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 标题
        Text(
            text = "Repositories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 仓库列表
        if (reposState.isNotEmpty()) {
            reposState.forEach { repo ->
                repo.name?.let {
                    RepositoryCard(
                        name = it,
                        description = repo.description,
                        language = repo.language,
                        stars = repo.stargazersCount ?: 0,
                        onClick = {
                            NavigationManager.navigateToRepoDetail(username, it)
                        }
                    )
                }
            }

            if (isLoadingMore) {
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
        } else if (!isLoadingMore) {
            // 空状态显示
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
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
@Composable
private fun UserList(
    users: List<SimpleUser>,
    viewModel: MineViewModel,
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
            .clickable { NavigationManager.navigateToUserProfile(user.login) },
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
                    text = user.login,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MineScreenPreview() {
    MaterialTheme {
        MineScreen()
    }
}