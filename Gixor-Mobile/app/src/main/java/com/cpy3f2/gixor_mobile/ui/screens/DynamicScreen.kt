package com.cpy3f2.gixor_mobile.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.entity.Event
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.utils.PreferencesManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun DynamicScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val hasToken = preferencesManager.hasToken()

    if (!hasToken) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Please login to view activities",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { NavigationManager.navigateToLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),  // 红色背景
                    contentColor = Color.White  // 白色文字
                ),
                shape = RoundedCornerShape(8.dp) // 圆角按钮
            ) {
                Text("Login")
            }
        }
        return
    }

    val followingList by vm.followingList.collectAsState()
    val isFollowingLoading by vm.isFollowingLoading.collectAsState()
    val receivedEvents by vm.receivedEvents.collectAsState()
    val relatedEvents by vm.relatedEvents.collectAsState()
    val isReceivedEventsLoading by vm.isReceivedEventsLoading.collectAsState()
    val isRelatedEventsLoading by vm.isRelatedEventsLoading.collectAsState()
    
    var selectedUser by remember { mutableStateOf<SimpleUser?>(null) }

    // 初始加载关注列表和所有动态
    LaunchedEffect(Unit) {
        vm.loadFollowingList()
        vm.gitHubUser.value?.data?.name?.let { vm.loadReceivedEvents(it) }
    }

    // 调试日志
    LaunchedEffect(selectedUser) {
        Log.d("DynamicScreen", "Selected user changed: ${selectedUser?.login}")
        selectedUser?.login?.let { username ->
            Log.d("DynamicScreen", "Loading events for user: $username")
            vm.loadRelatedEvents(username)
        }
    }

    // 添加日志以跟踪数据变化
    LaunchedEffect(relatedEvents) {
        Log.d("DynamicScreen", "Related events updated: ${relatedEvents.size} items")
    }

    LaunchedEffect(receivedEvents) {
        Log.d("DynamicScreen", "Received events updated: ${receivedEvents.size} items")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 关注者列表
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFollowingLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            } else {
                items(followingList) { user ->
                    FollowingItem(
                        user = user,
                        modifier = Modifier,
                        isSelected = selectedUser?.login == user.login,
                        onClick = {
                            Log.d("DynamicScreen", "User clicked: ${user.login}")
                            selectedUser = if (selectedUser?.login == user.login) {
                                null
                            } else {
                                user
                            }
                        }
                    )
                }
            }
        }

        HorizontalDivider()

        // 动态列表
        Box(modifier = Modifier.weight(1f)) {
            val currentEvents = if (selectedUser == null) receivedEvents else relatedEvents
            val isLoading = if (selectedUser == null) isReceivedEventsLoading else isRelatedEventsLoading

            Log.d("DynamicScreen", "Displaying events: ${currentEvents.size} items")
            
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            } else if (currentEvents.isEmpty()) {
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
                        text = if (selectedUser == null) "No events found" else "No events found for ${selectedUser?.login}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentEvents) { event ->
                        Log.d("DynamicScreen", "Rendering event: ${event.type} by ${event.actor.login}")
                        EventCard(event = event)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                event.actor.login.let { username ->
                                    NavigationManager.navigateToUserProfile(username)
                                }
                            }
                    ) {
                        AsyncImage(
                            model = event.actor.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(24.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Text(
                        text = event.actor.login,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { 
                            event.actor.login.let { username ->
                                NavigationManager.navigateToUserProfile(username)
                            }
                        }
                    )
                }
                Text(
                    text = event.type ,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = event.repo.name,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = event.createdAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 更新 FollowingItem 组件以处理空头像的情况
@Composable
fun FollowingItem(
    user: SimpleUser,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Avatar of ${user.login}",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .then(if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                } else Modifier),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = user.login,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp),
            textAlign = TextAlign.Center,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
