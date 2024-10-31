package com.cpy3f2.gixor_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        setContent {
            GixorMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //创建路由
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination ="login") {
                        composable("login") { LoginScreen(navController) }
                        composable("main") { MainFrame(navController) }
                        composable("search") { SearchScreen(navController)  }

                    }
                }
            }
        }
    }
}
