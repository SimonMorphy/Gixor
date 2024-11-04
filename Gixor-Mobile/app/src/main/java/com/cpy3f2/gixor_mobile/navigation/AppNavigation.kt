package com.cpy3f2.gixor_mobile.navigation

import AppDestinations
import ChatScreen
import RepoDetailScreen
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.net.Uri
import androidx.navigation.NavType
import com.cpy3f2.gixor_mobile.ui.screens.LoginScreen
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
    
    // 设置全局导航控制器
    LaunchedEffect(navController) {
        NavigationManager.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = AppDestinations.Main.route
    ) {
        composable(AppDestinations.Login.route) {
            LoginScreen(viewModel = viewModel)
        }
        composable(AppDestinations.Main.route) { MainFrame(navController) }
        composable(AppDestinations.Search.route) { SearchScreen(navController) }
        composable(AppDestinations.Message.route) { MessageScreen(viewModel = viewModel)  }
        composable(
            route = AppDestinations.NotificationDetail.route,
            arguments = listOf(
                navArgument("notificationId") { type = NavType.StringType },
                navArgument("repository") { 
                    type = NavType.StringType
                    nullable = true 
                }
            )
        ) { backStackEntry ->
            val notificationId = Uri.decode(backStackEntry.arguments?.getString("notificationId") ?: "")
            val repository = Uri.decode(backStackEntry.arguments?.getString("repository") ?: "")
            ChatScreen(
                viewModel = viewModel,
                notificationId = notificationId,
                repository = repository
            )
        }
        composable(
            route = AppDestinations.RepoDetail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            RepoDetailScreen(
                viewModel = viewModel,
                owner = owner,
                repoName = name
            )
        }
    }
} 