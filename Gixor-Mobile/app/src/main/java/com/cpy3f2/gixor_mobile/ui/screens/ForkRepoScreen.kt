import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForkRepoScreen(
    viewModel: MainViewModel,
    owner: String,
    repoName: String,
    modifier: Modifier = Modifier
) {
    var repoDescription by remember { mutableStateOf("") }
    val isLoading by viewModel.isForkLoading.collectAsState()
    val error by viewModel.forkError.collectAsState()

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

            // 描述文本框
            OutlinedTextField(
                value = repoDescription,
                onValueChange = { repoDescription = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // 错误提示
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Fork按钮
            Button(
                onClick = {
                    viewModel.createFork(owner, repoName, repoDescription)
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