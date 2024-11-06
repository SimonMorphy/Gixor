package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.model.entity.IssueDTO
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateIssueScreen(
    viewModel: MainViewModel,
    owner: String,
    repo: String,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    
    // 观察创建状态
    val isLoading by viewModel.isIssueCreating.collectAsState()
    val error by viewModel.issueCreationError.collectAsState()

    // 当错误状态改变时显示错误对话框
    LaunchedEffect(error) {
        if (error != null) {
            showErrorDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Issue") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 标题输入框
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // 内容输入框
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5
            )

            // 提交按钮
            Button(
                onClick = {
                    viewModel.createIssue(
                        owner = owner,
                        repo = repo,
                        issue = IssueDTO(
                            title = title,
                            body = body
                        )
                    ).also {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = title.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Submit new issue")
                }
            }
        }
    }

    // 成功对话框
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                NavigationManager.navigateBack()
            },
            title = { Text("Success") },
            text = { Text("Issue created successfully") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        NavigationManager.navigateBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // 错误对话框
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(error ?: "Unknown error occurred") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showErrorDialog = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
} 