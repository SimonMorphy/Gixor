import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.model.entity.GitHubRepository

@Composable
fun GitHubRepoItem(
    repo: GitHubRepository,
    onRepoClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onStarClick: () -> Unit,
    onLoginClick: () -> Unit,
    isStarred: Boolean,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onRepoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 仓库作者信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 作者头像
                AsyncImage(
                    model = repo.owner?.avatarUrl,
                    contentDescription = "Author avatar",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onAuthorClick)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 作者名称
                Text(
                    text = repo.owner?.login ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(onClick = onAuthorClick)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 仓库名称
            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            // 仓库描述
            if (!repo.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 底部信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧信息（语言、星标数、fork数）
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!repo.language.isNullOrEmpty()) {
                        Text(
                            text = repo.language,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.clickable(onClick = onLanguageClick)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${repo.stargazersCount ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Icon(
                        painter = painterResource(id = R.mipmap.fork),
                        contentDescription = "Forks",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${repo.forksCount ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                // 收藏按钮
                IconButton(
                    onClick = if (isLoggedIn) onStarClick else onLoginClick
                ) {
                    Icon(
                        imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (isStarred) "Unstar" else "Star",
                        tint = if (isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 