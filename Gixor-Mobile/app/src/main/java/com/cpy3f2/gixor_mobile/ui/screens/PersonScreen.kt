package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cpy3f2.gixor_mobile.ui.theme.GixorMobileTheme

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    userId: String // 用户ID作为参数传入
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item { ProfileHeader() }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { UserStats() }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { ContributionGraph() }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { RepositoryList() }
    }
}

@Composable
private fun ProfileHeader() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "用户头像URL",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "用户名",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "@用户ID",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "个人简介描述",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UserStats() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PersonStatItem(count = "100", label = "Repositories")
        PersonStatItem(count = "1.5k", label = "Followers")
        PersonStatItem(count = "500", label = "Following")
    }
}

@Composable
private fun PersonStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContributionGraph() {
    // TODO: 实现贡献图表
    // 可以使用自定义Canvas或第三方图表库
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Contributions",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun RepositoryList() {
    Column {
        Text(
            text = "Popular Repositories",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        // 添加示例仓库项
        repeat(3) { index ->
            RepositoryItem(
                name = "Example-Repo-${index + 1}",
                description = "This is a sample repository description for preview purposes. It demonstrates how the repository card will look in the actual app.",
                language = "Kotlin",
                stars = (100 * (index + 1)).toString()
            )
            if (index < 2) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun RepositoryItem(
    name: String,
    description: String,
    language: String,
    stars: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "●",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = language,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "★ $stars",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@PreviewLightDark
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PersonScreenPreview() {
    GixorMobileTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PersonScreen(
                userId = "octocat"
            )
        }
    }
}