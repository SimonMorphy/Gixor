package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.RateReview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import android.net.Uri


@Composable
fun MessageScreen(viewModel: MainViewModel) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.surface
    val isDarkIcons = !isSystemInDarkTheme()
    
    DisposableEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = isDarkIcons
        )
        onDispose {}
    }

    Scaffold(
        topBar = { NotificationsTopBar() },
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 过滤器
            item {
                NotificationFilters()
            }

            // 通知列表
            items(10) { index ->
                NotificationItem(
                    viewModel = viewModel,
                    repository = "android/architecture",
                    title = "Issue #${index + 100}: Update documentation for new features",
                    reason = if (index % 3 == 0) "mention" else if (index % 3 == 1) "review_requested" else "subscribed",
                    time = "2小时前",
                    isUnread = index % 2 == 0,
                    notificationId = "notification_$index"
                )
                
                if (index < 9) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun NotificationsTopBar() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { /* 标记所有已读 */ }) {
                    Icon(
                        imageVector = Icons.Rounded.DoneAll,
                        contentDescription = "Mark all as read"
                    )
                }
                
                IconButton(onClick = { /* ��置 */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationFilters() {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Unread", "Mentions")
    
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        ScrollableTabRow(
            selectedTabIndex = filters.indexOf(selectedFilter),
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[filters.indexOf(selectedFilter)]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            filters.forEach { filter ->
                Tab(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    text = {
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selectedFilter == filter) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    viewModel: MainViewModel,
    repository: String,
    title: String,
    reason: String,
    time: String,
    isUnread: Boolean,
    notificationId: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isUnread) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable { 
                // 使用新的路由定义进行导航
                viewModel.navController.value?.navigate(
                    AppDestinations.NotificationDetail.createRoute(
                        notificationId = notificationId,
                        repository = repository
                    )
                )
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 通知类型图标
        Icon(
            imageVector = when (reason) {
                "mention" -> Icons.Rounded.AlternateEmail
                "review_requested" -> Icons.Rounded.RateReview
                else -> Icons.Rounded.Notifications
            },
            contentDescription = null,
            tint = if (isUnread) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 仓库名
            Text(
                text = repository,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // 标题
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isUnread) FontWeight.Medium else FontWeight.Normal
            )
            
            // 时间和原因
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = reason.replace("_", " ").capitalize(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // 更多操作按钮
        IconButton(
            onClick = { /* 显示更多操作 */ }
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "More actions",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun MessageScreenPreview() {
    MessageScreen(viewModel = MainViewModel())
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    val previewViewModel = MainViewModel()
    Column {
        // 未读通知
        NotificationItem(
            viewModel = previewViewModel,
            repository = "android/architecture",
            title = "Issue #123: Update documentation for new features",
            reason = "mention",
            time = "2小时前",
            isUnread = true,
            notificationId = "notification_1"
        )
        
        Divider()
        
        // 已读通知
        NotificationItem(
            viewModel = previewViewModel,
            repository = "jetbrains/kotlin",
            title = "PR #456: Add support for new language features",
            reason = "review_requested",
            time = "昨天",
            isUnread = false,
            notificationId = "notification_2"
        )
    }
}