package com.cpy3f2.gixor_mobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cpy3f2.gixor_mobile.R
import com.cpy3f2.gixor_mobile.navigation.NavigationManager
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import kotlinx.coroutines.delay
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SplashScreen(viewModel: MainViewModel) {
    var remainingSeconds by remember { mutableIntStateOf(5) }
    var shouldNavigate by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }
    
    // 系统UI控制器
    val systemUiController = rememberSystemUiController()
    
    // 获取默认surface颜色
    val defaultStatusBarColor = MaterialTheme.colorScheme.surface
    
    // 设置状态栏
    DisposableEffect(systemUiController) {
        // 设置状态栏颜色为透明，文字为深色
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        
        onDispose {
            // 使用预先获取的颜色
            systemUiController.setStatusBarColor(
                color = defaultStatusBarColor,
                darkIcons = true
            )
        }
    }

    // 预加载数据
    LaunchedEffect(Unit) {
        viewModel.preloadData()
        isDataLoaded = true
    }

    // 倒计时和导航
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0 && !shouldNavigate) {
            delay(1000)
            remainingSeconds--
        }
        // 等待数据加载完成再导航
        if (isDataLoaded) {
            NavigationManager.navigateToMain()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 跳过按钮和倒计时
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${remainingSeconds}s",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "跳过",
                    modifier = Modifier
                        .clickable { 
                            if (isDataLoaded) {
                                shouldNavigate = true
                                NavigationManager.navigateToMain()
                            }
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 加载指示器
            if (!isDataLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }

            // Logo 和 Slogan
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.logo2),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    
                    Text(
                        text = "Where Geeks Inspire\nWhere Code Takes Higher",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
} 