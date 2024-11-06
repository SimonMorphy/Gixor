package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters
import com.cpy3f2.gixor_mobile.model.entity.IssueComment
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
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
    val comments by viewModel.issueComments.collectAsState()
    val isCommentsLoading by viewModel.isCommentsLoading.collectAsState()
    val userComments by viewModel.userComments.collectAsState()
    val isUserCommentsLoading by viewModel.isUserCommentsLoading.collectAsState()

    var commentText by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(owner, repo, issueNumber) {
        viewModel.loadIssueDetail(owner, repo, issueNumber)
        viewModel.loadIssueComments(owner, repo, issueNumber)
        viewModel.loadUserComments(owner, repo, issueNumber)
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
                },
                actions = {
                    Button(
                        onClick = {
                            NavigationManager.navigateToCreateIssue(owner, repo)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "New Issue",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Issue content in LazyColumn
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Issue 标题
                item {
                    var isEditing by remember { mutableStateOf(false) }
                    var editedTitle by remember { mutableStateOf(issue?.title ?: "") }
                    var showErrorDialog by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf("") }

                    // 检查当前用户是否有编辑权限
                    val currentUser = viewModel.gitHubUser.value?.data?.login
                    val canEdit = currentUser != null && (
                        currentUser == issue?.user?.login || // issue创建者
                        currentUser == owner // 仓库所有者
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            OutlinedTextField(
                                value = editedTitle,
                                onValueChange = { editedTitle = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                label = { Text("Issue Title") },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (editedTitle.isNotBlank()) {
                                            viewModel.updateIssue(
                                                owner = owner,
                                                repo = repo,
                                                issueNumber = issueNumber,
                                                title = editedTitle,
                                                onSuccess = {
                                                    isEditing = false
                                                },
                                                onError = { error ->
                                                    errorMessage = error
                                                    showErrorDialog = true
                                                }
                                            )
                                        }
                                    }
                                )
                            )
                        } else {
                            Text(
                                text = issue?.title ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        // 只有当用户有编辑权限时才显示编辑按钮
                        if (canEdit) {
                            IconButton(
                                onClick = {
                                    if (isEditing) {
                                        if (editedTitle.isNotBlank()) {
                                            viewModel.updateIssue(
                                                owner = owner,
                                                repo = repo,
                                                issueNumber = issueNumber,
                                                title = editedTitle,
                                                onSuccess = {
                                                    isEditing = false
                                                },
                                                onError = { error ->
                                                    errorMessage = error
                                                    showErrorDialog = true
                                                }
                                            )
                                        }
                                    } else {
                                        editedTitle = issue?.title ?: ""
                                        isEditing = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                                    contentDescription = if (isEditing) "Save" else "Edit Issue",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Error Dialog
                    if (showErrorDialog) {
                        AlertDialog(
                            onDismissRequest = { showErrorDialog = false },
                            title = { Text("Error") },
                            text = { Text(errorMessage) },
                            confirmButton = {
                                TextButton(onClick = { showErrorDialog = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }

                // Issue 状态
                item {
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = when(issue?.state?.lowercase()) {
                                "open" -> MaterialTheme.colorScheme.primary
                                "closed" -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Text(
                                text = issue?.state?.uppercase() ?: "",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "opened by ${issue?.user?.login} · ${
                                DateTimeConverters.formatDateTimeString(issue?.createdAt.toString())
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
                                    model = issue?.user?.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = issue?.user?.login ?: "",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = issue?.body ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Comments section
                item {
                    Text(
                        text = "Comments",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                // Comments list
                items(comments) { comment ->
                    val currentUser = viewModel.gitHubUser.value?.data?.login
                    val isUserComment = comment.user?.login == currentUser

                    CommentItem(
                        comment = comment,
                        isUserComment = isUserComment,
                        owner = owner,
                        repo = repo,
                        issueNumber = issueNumber,
                        viewModel = viewModel
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isCommentsLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            // Comment input section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        label = { Text("Add a comment") },
                        placeholder = { Text("Write your comment here...") }
                    )

                    Button(
                        onClick = {
                            viewModel.addComment(
                                owner = owner,
                                repo = repo,
                                issueNumber = issueNumber,
                                comment = commentText,
                                onSuccess = {
                                    commentText = ""
                                    showSuccessDialog = true
                                },
                                onError = { error ->
                                    errorMessage = error
                                    showErrorDialog = true
                                }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp),
                        enabled = commentText.isNotBlank()
                    ) {
                        Text("Comment")
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Comment added successfully") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun CommentItem(
    comment: IssueComment,
    isUserComment: Boolean,
    owner: String,
    repo: String,
    issueNumber: Long,
    viewModel: MainViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editedComment by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // 编辑对话框
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("编辑评论") },
            text = {
                OutlinedTextField(
                    value = editedComment,
                    onValueChange = { editedComment = it },
                    label = { Text("评论内容") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        comment.id?.let { commentId ->
                            viewModel.updateComment(
                                owner = owner,
                                repo = repo,
                                commentId = commentId,
                                body = editedComment,
                                issueNumber = issueNumber,
                                onSuccess = {
                                    showEditDialog = false
                                    showSuccessDialog = true
                                },
                                onError = {
                                    errorMessage = it
                                    showErrorDialog = true
                                }
                            )
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    // 成功提示对话框
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("成功") },
            text = { Text("评论更新成功") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("确定")
                }
            }
        )
    }

    // 错误提示对话框
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("错误") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("确定")
                }
            }
        )
    }

    // 删除确认对话框
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("确认删除") },
            text = { Text("确定要删除这条评论吗？") },
            confirmButton = {
                Button(
                    onClick = {
                        comment.id?.let { commentId ->
                            viewModel.deleteComment(
                                owner = owner,
                                repo = repo,
                                commentId = commentId,
                                issueNumber = issueNumber,
                                onSuccess = {
                                    showDeleteDialog = false
                                    showSuccessDialog = true
                                },
                                onError = {
                                    errorMessage = it
                                    showErrorDialog = true
                                }
                            )
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUserComment) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Comment header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧用户信息
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = comment.user?.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = comment.user?.login ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = DateTimeConverters.formatDateTimeString(comment.createdAt.toString()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                // 右侧更多按钮，仅在是用户自己的评论时显示
                if (isUserComment) {
                    var showMenu by remember { mutableStateOf(false) }
                    
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("编辑") },
                                onClick = {
                                    editedComment = extractCommentBody(comment.body ?: "")
                                    showEditDialog = true
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("删除") },
                                onClick = {
                                    showDeleteDialog = true
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Comment content
            Text(
                text = extractCommentBody(comment.body ?: ""),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// 处理评论内容的函数
fun extractCommentBody(rawBody: String): String {
    return try {
        // 尝试解析 JSON 格式
        val regex = "\\{\"body\":\"(.*)\"\\}".toRegex()
        val matchResult = regex.find(rawBody)
        matchResult?.groupValues?.get(1) ?: rawBody.trim('"')  // 如果解析失败，直接去掉首尾双引号
    } catch (e: Exception) {
        rawBody.trim('"')  // 发生异常时直接去掉首尾双引号
    }
} 