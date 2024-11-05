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
import com.cpy3f2.gixor_mobile.ui.screens.IssueDetailScreen
import com.cpy3f2.gixor_mobile.ui.screens.LoginScreen
import com.cpy3f2.gixor_mobile.ui.screens.MainFrame
import com.cpy3f2.gixor_mobile.ui.screens.MessageScreen
import com.cpy3f2.gixor_mobile.ui.screens.SearchScreen
import com.cpy3f2.gixor_mobile.ui.screens.UserProfileScreen
import com.cpy3f2.gixor_mobile.viewModels.MainViewModel
import com.cpy3f2.gixor_mobile.viewModels.UserProfileViewModel
import com.cpy3f2.gixor_mobile.ui.screens.PullRequestDetailScreen

@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    sharedPreferences: SharedPreferences,
    userModel: UserProfileViewModel
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
        composable(
            route = AppDestinations.UserProfile.route,
            arguments = listOf(
                navArgument("username") { 
                    type = NavType.StringType 
                }
            )
        ) { backStackEntry ->
            val username = Uri.decode(backStackEntry.arguments?.getString("username") ?: "")
            UserProfileScreen(
                username = username,
                viewModel = userModel
            )
        }
        composable(
            route = AppDestinations.IssueDetail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType },
                navArgument("issueNumber") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val issueNumber = backStackEntry.arguments?.getLong("issueNumber") ?: 0L
            
            IssueDetailScreen(
                viewModel = viewModel,
                owner = owner,
                repo = repo,
                issueNumber = issueNumber,
                onBackClick = { NavigationManager.navigateBack() }
            )
        }
        composable(
            route = AppDestinations.PullRequestDetail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType },
                navArgument("number") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            val prNumber = backStackEntry.arguments?.getLong("number") ?: 0L
            
            PullRequestDetailScreen(
                viewModel = viewModel,
                owner = owner,
                repo = repo,
                prNumber = prNumber,
                onBackClick = { NavigationManager.navigateBack() }
            )
        }
    }
} 