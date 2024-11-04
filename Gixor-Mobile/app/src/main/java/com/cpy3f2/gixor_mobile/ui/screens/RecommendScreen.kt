package com.cpy3f2.gixor_mobile.ui.screens

import RecommendedRepoItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
fun RecommendScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val trendyRepos by viewModel.trendyRepos.collectAsState()
    val starredRepos by viewModel.starredRepos.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    
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
                onStarClick = {
                    viewModel.toggleStarRepo(repo.author, repo.name, isStarred)
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
fun RecommendScreenPreview() {
    RecommendScreen(viewModel = MainViewModel())
}