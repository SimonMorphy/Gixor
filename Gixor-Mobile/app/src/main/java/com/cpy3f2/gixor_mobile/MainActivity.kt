package com.cpy3f2.gixor_mobile

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.cpy3f2.gixor_mobile.ui.screens.LoginScreen
import com.cpy3f2.gixor_mobile.ui.screens.MainFrame
import com.cpy3f2.gixor_mobile.ui.screens.SearchScreen
import com.cpy3f2.gixor_mobile.ui.theme.GixorMobileTheme
import okhttp3.RequestBody

class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .build()
        }
        // 如果 Application 还没初始化，使用 Activity 的 applicationContext
        if (MyApplication.Companion.instance == null) {
            MyApplication.Companion.instance = applicationContext as Application
        }
        setContent {
            GixorMobileTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sharedPreferences  = MyApplication.getApplicationContext().getSharedPreferences("token", MODE_PRIVATE)
                    //创建路由
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination ="main") {
                        composable("login") { LoginScreen(navController, sharedPreferences = sharedPreferences) }
                        composable("main") { MainFrame(navController) }
                        composable("search") { SearchScreen(navController)  }
                    }
                }
            }
        }
    }
}
