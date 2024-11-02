import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun GithubActivityItem(
    avatar: String,
    username: String,
    time: String,
    actionType: String,
    repoName: String,
    repoDescription: String? = null,
    language: String? = null,
    languageColor: Color? = null,
    starCount: Int = 0,
    onUserClick: () -> Unit = {},
    onRepoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 用户操作行
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            AsyncImage(
                model = avatar,
                contentDescription = "头像",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onUserClick),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 操作描述
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(username)
                    }
                    append(" $actionType ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(repoName)
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Star 数
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "stars",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = starCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        
        // 仓库卡片
        if (repoDescription != null || language != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onRepoClick),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E4E8))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // 仓库描述
                    if (repoDescription != null) {
                        Text(
                            text = repoDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // 仓库信息行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 编程语言
                        if (language != null && languageColor != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(languageColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = language,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // 时间
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// 预览
@Preview
@Composable
fun GithubActivityItemPreview() {
    GithubActivityItem(
        avatar = "https://github.com/avatar.jpg",
        username = "octocat",
        time = "2 hours ago",
        actionType = "starred",
        repoName = "facebook/react",
        repoDescription = "A declarative, efficient, and flexible JavaScript library for building user interfaces.",
        language = "JavaScript",
        languageColor = Color(0xFFF1E05A),
        starCount = 12500
    )
} 