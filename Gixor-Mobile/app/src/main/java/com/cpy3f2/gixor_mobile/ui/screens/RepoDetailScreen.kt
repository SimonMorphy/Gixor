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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material3.FilterChip
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.cpy3f2.gixor_mobile.model.entity.Issue
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.entity.PullRequest
import com.cpy3f2.gixor_mobile.navigation.NavigationManager

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
//                    (context as? Activity)?.finish()
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
                RepoTab.Code -> RepoCodeTab(owner, repoName, viewModel)
                RepoTab.Issues -> RepoIssuesTab(owner, repoName, viewModel)
                RepoTab.PullRequests -> RepoPullRequestsTab(owner, repoName, viewModel)
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
}

// 示例 Tab 内容
@Composable
fun RepoCodeTab(owner: String, repoName: String, viewModel: MainViewModel) {
    val repoDetails by viewModel.repoDetails.collectAsState()

    LaunchedEffect(owner, repoName) {
        viewModel.loadRepoDetails(owner, repoName)
    }

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
                // 仓库描述
                if (!repoDetails?.description.isNullOrEmpty()) {
                    Text(
                        text = repoDetails?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 统计信息
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Outlined.Star,
                        label = "Stars",
                        count = "${repoDetails?.stargazersCount ?: 0}"
                    )
                    StatItem(
                        icon = Icons.Outlined.AccountTree,
                        label = "Forks",
                        count = "${repoDetails?.forksCount ?: 0}"
                    )
                    StatItem(
                        icon = Icons.Outlined.RemoveRedEye,
                        label = "Issues",
                        count = "${repoDetails?.openIssues ?: 0}"
                    )
                }

                // 语言信息
                if (!repoDetails?.language.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Language: ${repoDetails?.language}",
                        style = MaterialTheme.typography.bodyMedium
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

@Composable
fun RepoIssuesTab(owner: String, repoName: String, viewModel: MainViewModel) {
    val issues by viewModel.repoIssues.collectAsState()
    val isLoading by viewModel.isIssuesLoading.collectAsState()
    var selectedFilter by remember { mutableStateOf("open") }

    LaunchedEffect(owner, repoName, selectedFilter) {
        viewModel.loadRepoIssues(owner, repoName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 筛选器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "open",
                onClick = { selectedFilter = "open" },
                label = { Text("Open") }
            )
            FilterChip(
                selected = selectedFilter == "closed",
                onClick = { selectedFilter = "closed" },
                label = { Text("Closed") }
            )
        }

        // 加载状态显示
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading issues...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else if (issues.isEmpty()) {
            // 无数据状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No issues found",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "There are no ${selectedFilter} issues in this repository",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            // Issues 列表
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(issues) { issue ->
                    IssueItem(
                        issue = issue,
                        onClick = {
                            issue.number?.let { number ->
                                NavigationManager.navigateToIssueDetail(
                                    owner = owner,
                                    repo = repoName,
                                    issueNumber = number
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IssueItem(
    issue: Issue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Issue 标题和编号
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = issue.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "#${issue.number}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Issue 状态和标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(issue.state ?: "")
                issue.labels?.take(3)?.forEach { label ->
                    LabelChip(label)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Issue 元信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Opened by ${issue.user?.login}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = DateTimeConverters.formatRelativeTime(issue.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (issue.comments ?: 0 > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Comment,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${issue.comments}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(state: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = when (state.lowercase()) {
            "open" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            "closed" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            else -> MaterialTheme.colorScheme.surface
        }
    ) {
        Text(
            text = state.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = when (state.lowercase()) {
                "open" -> MaterialTheme.colorScheme.primary
                "closed" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
fun LabelChip(label: Issue.Label) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = Color(android.graphics.Color.parseColor("#${label.color}")).copy(alpha = 0.2f)
    ) {
        Text(
            text = label.name ?: "",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(android.graphics.Color.parseColor("#${label.color}"))
        )
    }
}



@Composable
fun RepoPullRequestsTab(owner: String, repoName: String, viewModel: MainViewModel) {
    val pullRequests by viewModel.repoPullRequests.collectAsState()
    val isLoading by viewModel.isPrLoading.collectAsState()
    var selectedFilter by remember { mutableStateOf("all") }

    LaunchedEffect(owner, repoName, selectedFilter) {
        viewModel.loadRepoPullRequests(owner, repoName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 筛选器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text("All") }
            )
            FilterChip(
                selected = selectedFilter == "open",
                onClick = { selectedFilter = "open" },
                label = { Text("Open") }
            )
            FilterChip(
                selected = selectedFilter == "closed",
                onClick = { selectedFilter = "closed" },
                label = { Text("Closed") }
            )
        }

        // 加载状态显示
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading pull requests...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else if (pullRequests.isEmpty()) {
            // 无数据状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountTree,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "No pull requests found",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "There are no ${if (selectedFilter == "all") "" else "$selectedFilter "}pull requests in this repository",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            // PR 列表
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pullRequests) { pr ->
                    PullRequestItem(pr)
                }
            }
        }
    }
}

@Composable
fun PullRequestItem(pr: PullRequest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // PR 标题和编号
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pr.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "#${pr.number}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // PR 状态和元信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 状态标签
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when (pr.state) {
                        "open" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        "closed" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = when (pr.state) {
                            "open" -> "开放"
                            "closed" -> "已关闭"
                            else -> pr.state ?: ""
                        },
                        color = when (pr.state) {
                            "open" -> MaterialTheme.colorScheme.primary
                            "closed" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // 草稿标签
                if (pr.isDraft == true) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "草稿",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // PR 创建者和时间信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 用户头像
                AsyncImage(
                    model = pr.user?.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                // 用户名
                Text(
                    text = pr.user?.login ?: "未知用户",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // 创建时间
                Text(
                    text = DateTimeConverters.formatRelativeTime(pr.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}