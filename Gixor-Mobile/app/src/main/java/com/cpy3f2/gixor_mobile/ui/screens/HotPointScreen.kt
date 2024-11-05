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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

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
    
    // 监听 UI 状态变化
    LaunchedEffect(uiState) {
        when (uiState) {
            is MainViewModel.UiState.Success -> {
                // 可以添加一些成功的反馈，比如显示 Snackbar
            }
            is MainViewModel.UiState.Error -> {
                // 显示错误信息
            }
            else -> {}
        }
    }
    
    LaunchedEffect(Unit) {
        if (trendyRepos.isEmpty()) {
            viewModel.loadTrendyRepos()
        }
        if (isLoggedIn && starredRepos.isEmpty()) {
            viewModel.loadStarredRepos()
        }
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(trendyRepos) { repo ->
            val repoId = "${repo.author}/${repo.name}"
            val isStarred = starredRepos.contains(repoId)
            
            RecommendedRepoItem(
                repo = repo,
                onRepoClick = {
                    NavigationManager.navigateToRepoDetail(repo.author, repo.name)
                },
                onAuthorClick = {
                    repo.author?.let { username ->
                        NavigationManager.navigateToUserProfile(username)
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
@Preview
@Composable
fun HotPointPreview() {
    HotPointScreen(viewModel = MainViewModel())
}