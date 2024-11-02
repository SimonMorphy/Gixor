package com.cpy3f2.gixor_mobile.ui.screens

import LoginWebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var showLoginWebView by remember { mutableStateOf(true) }

    LoginWebView(
        viewModel = viewModel,
        onLoginSuccess = {
            showLoginWebView = false
            viewModel.navigateToMain()
        }
    )
}
