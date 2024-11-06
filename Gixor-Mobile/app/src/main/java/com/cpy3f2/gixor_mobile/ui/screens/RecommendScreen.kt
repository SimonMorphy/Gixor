package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters
import com.cpy3f2.gixor_mobile.model.entity.Event
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun RecommendScreen(vm: MainViewModel) {
    val events by vm.publicEvents.collectAsState()
    val isLoading by vm.isEventsLoading.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadPublicEvents()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (events.isEmpty()) {
            // 添加空数据状态提示
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "No events found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Pull down to refresh",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { vm.loadPublicEvents() }
                ) {
                    Text("Retry")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = events) { event ->
                    EventItem(event = event)
                }
            }
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 用户信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 用户头像
                AsyncImage(
                    model = event.actor.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable {
                            event.actor.login?.let { username ->
                                NavigationManager.navigateToUserProfile(username)
                            }
                        },
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 用户名和事件类型
                Column {
                    Text(
                        text = event.actor.login ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.clickable {
                            event.actor.login?.let { username ->
                                NavigationManager.navigateToUserProfile(username)
                            }
                        }
                    )
                    
                    Text(
                        text = getEventDescription(event),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 仓库信息
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        // 可以添加点击跳转到仓库详情
                        val (owner, repo) = event.repo.name.split("/")
                        NavigationManager.navigateToRepoDetail(owner, repo)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = event.repo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // 时间信息
            Text(
                text =  DateTimeConverters.formatRelativeTimeFromString(event.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// 获取事件描述
private fun getEventDescription(event: Event): String {
    return when (event.type) {
        "PushEvent" -> "pushed to ${event.payload.ref?.removePrefix("refs/heads/")}"
        "CreateEvent" -> "created ${event.payload.ref} ${if (event.payload.ref == "repository") "" else "in"}"
        "WatchEvent" -> "starred"
        "ForkEvent" -> "forked"
        "IssuesEvent" -> "${event.payload.action} issue"
        "IssueCommentEvent" -> "commented on issue"
        "PullRequestEvent" -> "${event.payload.action} pull request"
        else -> event.type.removeSuffix("Event").toLowerCase()
    }
}
