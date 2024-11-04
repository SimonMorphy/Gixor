package com.cpy3f2.gixor_mobile.ui.screens

import LoginWebView
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var showLoginWebView by remember { mutableStateOf(true) }
    
    // 记住系统UI控制器
    val systemUiController = rememberSystemUiController()
    
    // 设置状态栏
    DisposableEffect(systemUiController) {
        // 设置状态栏为透明，图标为深色
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        
        onDispose {
            // 重置状态栏
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { paddingValues ->
        LoginWebView(
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel,
            onLoginSuccess = {
                showLoginWebView = false
                viewModel.navigateToMain()
            }
        )
    }
}
