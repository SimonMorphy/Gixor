import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.entity.TrendyRepository
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import com.cpy3f2.gixor_mobile.model.entity.Contributor

// 创建 Preview 参数提供者
class TrendyRepoPreviewProvider : PreviewParameterProvider<TrendyRepository> {
    override val values = sequenceOf(
        TrendyRepository(
            author = "google",
            repoName = "androidx",
            avatar = "https://github.com/google.png",
            url = "https://github.com/google/androidx",
            description = "Development environment for Android Jetpack extension libraries under androidx namespace. Synchronized with Android Jetpack's primary development branch on AOSP.",
            language = "Kotlin",
            languageColor = "#A97BFF",
            stars = 5678,
            forks = 1234,
            currentPeriodStars = 123,
            builtBy = listOf(
                Contributor(
                    username = "googledev",
                    href = "https://github.com/googledev",
                    avatar = "https://avatars.githubusercontent.com/u/1234567"
                )
            )
        ),
        // 添加一个最小数据的示例
        TrendyRepository(
            author = "minimal-repo",
            repoName = "test-repo",
            avatar = null.toString(),
            url = "https://github.com/test/test",
            description = null,
            language = null,
            languageColor = null,
            stars = 0,
            forks = 0,
            currentPeriodStars = 0,
            builtBy = emptyList()
        )
    )
}

// 添加深色主题预览
@Preview(
    name = "Trendy Repo Dark",
    showBackground = true,
    backgroundColor = 0xFF1C1B1F,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RecommendedRepoItemDarkPreview(
    @PreviewParameter(TrendyRepoPreviewProvider::class) repo: TrendyRepository
) {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface {
            RecommendedRepoItem(
                repo = repo,
                onRepoClick = {}
            )
        }
    }
}

// 添加浅色主题预览
@Preview(
    name = "Trendy Repo Light",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun RecommendedRepoItemLightPreview(
    @PreviewParameter(TrendyRepoPreviewProvider::class) repo: TrendyRepository
) {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Surface {
            RecommendedRepoItem(
                repo = repo,
                onRepoClick = {}
            )
        }
    }
}

// 添加多个项目的列表预览
@Preview(
    name = "Trendy Repo List",
    showBackground = true,
    heightDp = 500
)
@Composable
fun RecommendedRepoListPreview() {
    MaterialTheme {
        Surface {
            LazyColumn {
                items(TrendyRepoPreviewProvider().values.toList()) { repo ->
                    RecommendedRepoItem(
                        repo = repo,
                        onRepoClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedRepoItem(
    repo: TrendyRepository,
    onRepoClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onRepoClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE1E4E8))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Author info row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = repo.avatar,
                    contentDescription = "Author avatar",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
//                    error = painterResource(id = R.drawable.default_avatar)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = repo.author ?: "Unknown Author",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Repository name
            Text(
                text = repo.repoName ?: "Unnamed Repository",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            // Description
            if (!repo.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language
                if (!repo.language.isNullOrEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(
                                    try {
                                        Color(android.graphics.Color.parseColor(repo.languageColor ?: "#CCCCCC"))
                                    } catch (e: Exception) {
                                        Color.Gray
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = repo.language,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                // Stars
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
                        text = "${repo.stars ?: 0}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Current period stars
                if ((repo.currentPeriodStars ?: 0) > 0) {
                    Text(
                        text = "+${repo.currentPeriodStars} stars today",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF28a745)
                    )
                }
            }
        }
    }
} 