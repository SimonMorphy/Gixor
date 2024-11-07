package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.RateReview
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters
import com.cpy3f2.gixor_mobile.model.entity.Notification

enum class NotificationFilter {
    ALL, UNREAD, MENTIONS
}

@Composable
fun MessageScreen(viewModel: MainViewModel) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isNotificationsLoading.collectAsState()
    val hasError by viewModel.notificationError.collectAsState()
    val listState = rememberLazyListState()
    var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }

    // 首次进入页面时加载数据
    LaunchedEffect(Unit) {
        viewModel.loadNotifications(isRefresh = true)
    }

    val filteredNotifications = when (selectedFilter) {
        NotificationFilter.UNREAD -> notifications.filter { it.unread }
        NotificationFilter.MENTIONS -> notifications.filter { it.reason == "mention" }
        NotificationFilter.ALL -> notifications
    }

    val swipeRefreshState = rememberSwipeRefreshState(isLoading)

    Scaffold(
        topBar = { NotificationsTopBar(viewModel) },
        modifier = Modifier.statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs always visible at the top
            NotificationFilters(
                selectedFilter = selectedFilter,
                onFilterSelected = { newFilter -> 
                    selectedFilter = newFilter
                    viewModel.loadNotifications(isRefresh = true)
                }
            )

            // Content area below tabs
            Box(modifier = Modifier.weight(1f)) {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.loadNotifications(isRefresh = true) }
                ) {
                    when {
                        hasError != null -> ErrorState(
                            error = hasError!!,
                            onRetry = { viewModel.loadNotifications(isRefresh = true) }
                        )
                        filteredNotifications.isEmpty() && !isLoading -> EmptyState()
                        else -> LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredNotifications.size) { index ->
                                val notification = filteredNotifications[index]
                                NotificationItem(
                                    viewModel = viewModel,
                                    repository = notification.repository?.fullName ?: "",
                                    title = notification.subject?.title ?: "",
                                    reason = notification.reason ?: "",
                                    time = DateTimeConverters.formatRelativeTime(notification.updatedAt),
                                    isUnread = notification.unread,
                                    notificationId = notification.id ?: "",
                                    notification = notification,
                                    selectedFilter = selectedFilter
                                )

                                if (index < filteredNotifications.size - 1) {
                                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                                }
                            }

                            if (isLoading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.surfaceTint
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationsTopBar(viewModel: MainViewModel) {
    val isMarkingAllAsRead by viewModel.isMarkingAllAsRead.collectAsState()

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
                // 一键已读按钮
                IconButton(
                    onClick = { viewModel.markAllNotificationsAsRead() },
                    enabled = !isMarkingAllAsRead
                ) {
                    if (isMarkingAllAsRead) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.DoneAll,
                            contentDescription = "Mark all as read"
                        )
                    }
                }

                IconButton(onClick = { /* 设置 */ }) {
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
private fun NotificationFilters(
    selectedFilter: NotificationFilter,
    onFilterSelected: (NotificationFilter) -> Unit
) {
    val filters = NotificationFilter.values()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedFilter.ordinal,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFilter.ordinal]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            filters.forEach { filter ->
                Tab(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    text = {
                        Text(
                            text = when (filter) {
                                NotificationFilter.ALL -> "All"
                                NotificationFilter.UNREAD -> "Unread"
                                NotificationFilter.MENTIONS -> "Mentions"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selectedFilter == filter) 
                                FontWeight.Medium else FontWeight.Normal
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
    notificationId: String,
    notification: Notification,
    selectedFilter: NotificationFilter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isUnread) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface
            )
            .clickable {
                //todo 有机会做
//                notification.subject?.let { subject ->
//                    val urlParts = subject.url?.split("/")
//                    val owner = urlParts?.getOrNull(4)
//                    val repo = urlParts?.getOrNull(5)
//                    val  number = urlParts?.getOrNull(7)?.toIntOrNull()
//
//                    when (subject.type) {
//                        "Issue" -> NavigationManager.navigateToIssueDetail(
//                            owner = owner ?: "",
//                            repo = repo ?: "",
//                            issueNumber = number ?: 0
//                        )
//                        "PullRequest" -> NavigationManager.navigateToPullRequestDetail(
//                            owner = owner ?: "",
//                            repo = repo ?: "",
//                            pullRequestNumber = ZZ ?: 0
//                        )
//                        "Discussion" -> NavigationManager.navigateToDiscussion(
//                            owner = owner ?: "",
//                            repo = repo ?: "",
//                            discussionNumber = subject.number ?: 0
//                        )
//                    }
//                }
//                viewModel.markNotificationAsRead(notificationId)
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

        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = { /* 显示更多操作 */ }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "More actions",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Only show red dot for unread notifications in ALL filter
            if (isUnread && selectedFilter == NotificationFilter.ALL) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "暂无通知",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("重")
            }
        }
    }
}

// 扩展函数：检查是否滚动到底部
fun LazyListState.isScrolledToEnd(): Boolean {
    val lastItem = layoutInfo.totalItemsCount - 1
    return lastItem > 0 && firstVisibleItemIndex + layoutInfo.visibleItemsInfo.size >= lastItem
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
            notificationId = "notification_1",
            notification = Notification(),
            selectedFilter = NotificationFilter.ALL
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
            notificationId = "notification_2",
            notification = Notification(),
            selectedFilter = NotificationFilter.ALL
        )
    }
}