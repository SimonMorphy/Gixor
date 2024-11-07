package com.cpy3f2.gixor_mobile.ui.screens

import RecommendedRepoItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment

/**
 * 推荐界面
 */
@Composable
fun HotPointScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val trendyRepos by viewModel.trendyRepos.collectAsState()
    val starredRepos by viewModel.starredRepos.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val showLoginDialog by viewModel.showLoginDialog.collectAsState()

    // 当进入页面时加载数据
    LaunchedEffect(Unit) {
        viewModel.loadTrendyRepos()
        if (isLoggedIn) {
            viewModel.loadStarredRepos()
        }
    }

    // 添加登录对话框
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideLoginDialog() },
            title = { Text("需要登录") },
            text = { Text("请先登录后再查看用户详情") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.hideLoginDialog()
                    viewModel.navigateToLogin()
                }) {
                    Text("去登录")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideLoginDialog() }) {
                    Text("取消")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is MainViewModel.UiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(trendyRepos) { repo ->
                        val repoId = "${repo.author}/${repo.name}"
                        val isStarred = starredRepos.contains(repoId)

                        RecommendedRepoItem(
                            repo = repo,
                            onRepoClick = {
                                if (viewModel.checkLoginStatus()) {
                                    NavigationManager.navigateToRepoDetail(repo.author, repo.name)
                                } else {
                                    viewModel.showLoginDialog()
                                }
                            },
                            onAuthorClick = {
                                repo.author?.let { username ->
                                    if (viewModel.checkLoginStatus()) {
                                        NavigationManager.navigateToUserProfile(username)
                                    } else {
                                        viewModel.showLoginDialog()
                                    }
                                }
                            },
                            onLanguageClick = {
                                // NavigationManager.navigateToLanguageSearch(repo.language)
                            },
                            onStarClick = {
                                if (isLoggedIn) {
                                    viewModel.toggleStarRepo(repo.author, repo.name, isStarred)
                                } else {
                                    viewModel.navigateToLogin()
                                }
                            },
                            onLoginClick = {
                                viewModel.navigateToLogin()
                            },
                            isStarred = isStarred,
                            isLoggedIn = isLoggedIn
                        )

                    }
                }
            }
        }
    }
}
