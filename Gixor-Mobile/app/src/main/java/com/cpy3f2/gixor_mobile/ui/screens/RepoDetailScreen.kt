import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import android.content.res.Configuration
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    viewModel: MainViewModel,
    owner: String,
    repoName: String,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(RepoTab.Code) }
    val isStarred by viewModel.starredRepos.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val context = LocalContext.current
    
    Column(modifier = modifier.fillMaxSize()) {
        // 顶部应用栏
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = repoName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = owner,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { 
                    (context as? Activity)?.finish()
                }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            actions = {
                // Star 按钮
                IconButton(
                    onClick = {
                        if (isLoggedIn) {
                            viewModel.toggleStarRepo(owner, repoName, isStarred.contains("$owner/$repoName"))
                        } else {
                            viewModel.navigateToLogin()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isStarred.contains("$owner/$repoName")) {
                                R.mipmap.starf
                            } else {
                                R.mipmap.star
                            }
                        ),
                        contentDescription = "Star repository"
                    )
                }
            }
        )

        // 标签栏
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            edgePadding = 16.dp
        ) {
            RepoTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.title) }
                )
            }
        }

        // 内容区域
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                RepoTab.Code -> RepoCodeTab(owner, repoName)
                else -> {
                    // 其他标签页显示开发中提示
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Coming Soon",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This feature is under development",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

enum class RepoTab(val title: String) {
    Code("Code"),
    Issues("Issues"),
    PullRequests("Pull Requests"),
    Discussions("Discussions"),
    Actions("Actions"),
    Projects("Projects"),
    Security("Security"),
    Insights("Insights")
}

// 示例 Tab 内容
@Composable
fun RepoCodeTab(owner: String, repoName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 仓库信息卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // 统计信息
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Outlined.Star,
                        label = "Stars",
                        count = "1.2k"
                    )
                    StatItem(
                        icon = Icons.Outlined.AccountTree,
                        label = "Forks",
                        count = "234"
                    )
                    StatItem(
                        icon = Icons.Outlined.RemoveRedEye,
                        label = "Watching",
                        count = "45"
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 文件列表
        LazyColumn {
            items(sampleFiles) { file ->
                FileListItem(file)
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    count: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = count,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun FileListItem(file: RepoFile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = file.name)
    }
}

data class RepoFile(
    val name: String,
    val isDirectory: Boolean
)

val sampleFiles = listOf(
    RepoFile(".github", true),
    RepoFile("src", true),
    RepoFile("tests", true),
    RepoFile(".gitignore", false),
    RepoFile("README.md", false),
    RepoFile("LICENSE", false)
)

@Preview(
    name = "Repo Detail Light Theme",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun RepoDetailScreenLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            RepoDetailScreen(
                viewModel = MainViewModel(),
                owner = "google",
                repoName = "androidx"
            )
        }
    }
}

@Preview(
    name = "Repo Detail Dark Theme",
    showBackground = true,
    backgroundColor = 0xFF1C1B1F,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RepoDetailScreenDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            RepoDetailScreen(
                viewModel = MainViewModel(),
                owner = "google",
                repoName = "androidx"
            )
        }
    }
} 