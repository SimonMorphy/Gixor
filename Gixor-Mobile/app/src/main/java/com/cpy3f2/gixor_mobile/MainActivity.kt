package com.cpy3f2.gixor_mobile


import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.cpy3f2.gixor_mobile.navigation.AppNavigation

import com.cpy3f2.gixor_mobile.ui.theme.GixorMobileTheme
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import com.cpy3f2.gixor_mobile.viewModels.MineViewModel
import com.cpy3f2.gixor_mobile.viewModels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
        // 使用 getInstance() 方法获取实例
        val app = MyApplication.getInstance()

        // 创建 ViewModel
        val viewModel: MainViewModel by viewModels()
        val userModel: UserProfileViewModel by viewModels()
        val mineModel: MineViewModel by viewModels()

        setContent {
            GixorMobileTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        viewModel = viewModel,
                        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE),
                        userModel = userModel,
                        mineModel = mineModel
                    )
                }
            }
        }
    }
}