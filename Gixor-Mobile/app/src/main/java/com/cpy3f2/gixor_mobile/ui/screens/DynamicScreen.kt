package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.cpy3f2.gixor_mobile.model.entity.SimpleUser
import com.cpy3f2.gixor_mobile.navigation.NavigationManager

@Composable
fun DynamicScreen(vm: MainViewModel) {
    val followingList by vm.followingList.collectAsState()
    val isFollowingLoading by vm.isFollowingLoading.collectAsState()

    LaunchedEffect(Unit) {
        if (vm.hasToken()) {
            vm.loadFollowingList()
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if (vm.hasToken()) {
            // 关注者列表
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 12.dp, top = 8.dp)
            ) {
                if (isFollowingLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(24.dp)
                        )
                    }
                } else {
                    items(followingList) { user ->
                        FollowingItem(
                            user = user,
                            modifier = Modifier.padding(end = 16.dp),
                            onClick = {
                                // 导航到用户详情页面
                                user.login?.let { username ->
                                    NavigationManager.navigateToUserProfile(username)
                                }
                            }
                        )
                    }
                }
            }
        } else {
            // 未登录状态展示...
        }
        
        HorizontalDivider()
        LazyColumn {
            // LazyColumn的内容...
        }
    }
}

@Composable
fun FollowingItem(
    user: SimpleUser,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 头像
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Avatar of ${user.login}",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 用户名
        Text(
            text = user.login ?: "",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp),
            textAlign = TextAlign.Center
        )
    }
}
