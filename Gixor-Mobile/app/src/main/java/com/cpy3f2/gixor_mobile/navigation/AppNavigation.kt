package com.cpy3f2.gixor_mobile.navigation

import AppDestinations
import ChatScreen
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.net.Uri
import androidx.navigation.NavType
import com.cpy3f2.gixor_mobile.ui.screens.MainFrame
import com.cpy3f2.gixor_mobile.ui.screens.MessageScreen
import com.cpy3f2.gixor_mobile.ui.screens.SearchScreen
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel

@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    sharedPreferences: SharedPreferences
) {
    val navController = rememberNavController()
    
    // 设置导航控制器
    LaunchedEffect(navController) {
        viewModel.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = AppDestinations.Main.route
    ) {
//        composable(AppDestinations.Login.route) {
//            LoginScreen(
//            )
//        }
        composable(AppDestinations.Main.route) { MainFrame(navController) }
        composable(AppDestinations.Search.route) { SearchScreen(navController) }
        composable(AppDestinations.Message.route) { MessageScreen(viewModel = viewModel)  }
        composable(
            route = AppDestinations.Chat.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = Uri.decode(backStackEntry.arguments?.getString("userId") ?: "")
            val userName = Uri.decode(backStackEntry.arguments?.getString("userName") ?: "")
            ChatScreen(
                viewModel = viewModel,
                userId = userId,
                userName = userName
            )
        }
    }
} 