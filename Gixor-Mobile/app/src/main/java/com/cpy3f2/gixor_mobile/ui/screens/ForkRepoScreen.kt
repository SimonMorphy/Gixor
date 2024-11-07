import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cpy3f2.Gixor.Domain.DTO.ForkDTO
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import com.cpy3f2.gixor_mobile.viewModels.MineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForkRepoScreen(
    viewModel: MainViewModel,
    mineViewModel: MineViewModel,
    owner: String,
    repoName: String,
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isForkLoading.collectAsState()
    val error by viewModel.forkError.collectAsState()
    val currentUser by mineViewModel.userProfile.collectAsState()
    var newRepoName by remember { mutableStateOf(repoName) }

    var defaultBranchOnly by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxSize()) {
        // 顶部应用栏
        TopAppBar(
            title = { Text("Create a new fork") },
            navigationIcon = {
                IconButton(onClick = { NavigationManager.navigateBack() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 仓库信息
            Text(
                text = "You're about to fork $owner/$repoName",
                style = MaterialTheme.typography.titleMedium
            )

            // 组织选择
            OutlinedTextField(
                value = currentUser?.login ?: "",
                onValueChange = { /* 不允许修改 */ },
                label = { Text("Owner") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // 仓库名字段（可编辑）
            OutlinedTextField(
                value = newRepoName,
                onValueChange = { newRepoName = it },
                label = { Text("Repository name") },
                modifier = Modifier.fillMaxWidth()
            )

            // 默认分支选项
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = defaultBranchOnly,
                    onCheckedChange = { defaultBranchOnly = it }
                )
                Text(
                    text = "Copy the default branch only",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 错误提示
            error?.let { errorMessage ->
                Text(
                    text = if (errorMessage.contains("500")) {
                        "Fork created successfully" // Server returns 500 but fork is actually created
                    } else {
                        errorMessage
                    },
                    color = if (errorMessage.contains("500")) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Fork按钮
            Button(
                onClick = {
                    val forkDTO = ForkDTO(
                        name = newRepoName,
                        defaultBranchOnly = defaultBranchOnly
                    )
                    viewModel.createFork(owner, repoName, forkDTO)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create fork")
                }
            }

            // 监听fork结果
            LaunchedEffect(error) {
                error?.let { errorMessage ->
                    if (errorMessage.contains("500")) {
                        // Fork成功，返回上一页
                        NavigationManager.navigateBack()
                    }
                }
            }

            // 提示信息
            Text(
                text = "By creating a fork, you'll be making a copy of $owner/$repoName in your account.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 