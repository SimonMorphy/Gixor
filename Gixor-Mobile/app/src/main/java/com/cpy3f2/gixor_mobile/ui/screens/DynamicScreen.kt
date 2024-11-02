package com.cpy3f2.gixor_mobile.ui.screens

import LoginWebView
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cpy3f2.gixor_mobile.ui.components.FocusContent
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun DynamicScreen(vm: MainViewModel = viewModel()) {
    var showLoginWebView by remember { mutableStateOf(false) }

    if (showLoginWebView) {
        LoginWebView(
            viewModel = vm,
            onLoginSuccess = {
                showLoginWebView = false
                // 可以添加登录成功的提示
            }
        )
    } else {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (vm.hasToken()) {
                // 原有的已登录内容
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 12.dp, top = 8.dp)
                ) {
                    items(vm.focusData.size) { index ->
                        FocusContent(
                            focusItem = vm.focusData[index],
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            } else {
                // 未登录状态的展示
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "未登录",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { 
                                vm.navigateToLogin()
                            }
                        ) {
                            Text("去登录")
                        }
                    }
                }
            }

            HorizontalDivider()
            LazyColumn {
                // LazyColumn的内容
            }
        }
    }
}



@Preview
@Composable
fun DynamicScreenPreview() {
    DynamicScreen()
}
