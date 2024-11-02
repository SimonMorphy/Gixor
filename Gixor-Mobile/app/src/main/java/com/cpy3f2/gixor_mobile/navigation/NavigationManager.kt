package com.cpy3f2.gixor_mobile.navigation

import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NavigationManager {
    private val _navController = MutableStateFlow<NavHostController?>(null)
    val navController: StateFlow<NavHostController?> = _navController.asStateFlow()

    fun setNavController(navController: NavHostController) {
        _navController.value = navController
    }

    fun navigateToLogin() {
        _navController.value?.navigate(AppDestinations.Login.route)
    }

    fun navigateToMain() {
        _navController.value?.navigate(AppDestinations.Main.route)
    }

    fun navigateToSearch() {
        _navController.value?.navigate(AppDestinations.Search.route)
    }

    fun navigateToMessage() {
        _navController.value?.navigate(AppDestinations.Message.route)
    }

    fun navigateToChat(userId: String, userName: String) {
        _navController.value?.navigate(AppDestinations.Chat.createRoute(userId, userName))
    }

    fun navigateToNotification(notificationId: String, repository: String? = null) {
        _navController.value?.navigate(
            AppDestinations.NotificationDetail.createRoute(
                notificationId = notificationId,
                repository = repository
            )
        )
    }

    fun navigateBack() {
        _navController.value?.popBackStack()
    }
} 