import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun DynamicItem(
    avatar: String,
    username: String,
    time: String,
    content: String,
    imageUrls: List<String> = emptyList(),
    likeCount: Int = 0,
    commentCount: Int = 0,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 用户信息行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧用户信息
            Row(
                modifier = Modifier.clickable(onClick = onUserClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                AsyncImage(
                    model = avatar,
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 用户名和时间
                Column {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            // 右侧更多按钮
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* 更多操作 */ },
                tint = Color.Gray
            )
        }
        
        // 动态内容
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        
        // 图片网格（如果有图片）
        if (imageUrls.isNotEmpty()) {
            ImageGrid(imageUrls = imageUrls)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 底部操作栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 点赞
            Row(
                modifier = Modifier.clickable(onClick = onLikeClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "点赞",
                    tint = if (isLiked) Color.Red else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likeCount.toString(),
                    color = if (isLiked) Color.Red else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // 评论
            Row(
                modifier = Modifier.clickable(onClick = onCommentClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "评论",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = commentCount.toString(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // 分享
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "分享",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onShareClick)
            )
        }
    }
}

@Composable
private fun ImageGrid(
    imageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val imageCount = imageUrls.size
    
    LazyVerticalGrid(
        columns = Fixed(if (imageCount == 4) 2 else 3),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(imageUrls) { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// 使用示例
@Preview
@Composable
fun DynamicItemPreview() {
    DynamicItem(
        avatar = "https://placekitten.com/200/200",
        username = "用户名",
        time = "2小时前",
        content = "这是一条动态内容，可以包含很多文字...",
        imageUrls = listOf(
            "https://placekitten.com/300/300",
            "https://placekitten.com/301/301",
            "https://placekitten.com/302/302"
        ),
        likeCount = 128,
        commentCount = 32,
        isLiked = true
    )
} 