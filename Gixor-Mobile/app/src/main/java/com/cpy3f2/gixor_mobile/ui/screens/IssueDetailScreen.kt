package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    viewModel: MainViewModel,
    owner: String,
    repo: String,
    issueNumber: Long,
    onBackClick: () -> Unit
) {
    val issue by viewModel.currentIssue.collectAsState()
    val isLoading by viewModel.isIssueDetailLoading.collectAsState()

    LaunchedEffect(owner, repo, issueNumber) {
        viewModel.loadIssueDetail(owner, repo, issueNumber)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "$owner/$repo",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "#$issueNumber",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                            text = "Loading issue details...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                issue?.let { currentIssue ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Issue 标题
                        item {
                            Text(
                                text = currentIssue.title ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // Issue 状态
                        item {
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = when(currentIssue.state?.lowercase()) {
                                        "open" -> MaterialTheme.colorScheme.primary
                                        "closed" -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ) {
                                    Text(
                                        text = currentIssue.state?.uppercase() ?: "",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "opened by ${currentIssue.user?.login} · ${
                                        DateTimeConverters.formatRelativeTime(currentIssue.createdAt)
                                    }",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // Issue 内容
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = currentIssue.user?.avatarUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = currentIssue.user?.login ?: "",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    
                                    Text(
                                        text = currentIssue.body ?: "",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 