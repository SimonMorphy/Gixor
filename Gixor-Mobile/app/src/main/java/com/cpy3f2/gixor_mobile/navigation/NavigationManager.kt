package com.cpy3f2.gixor_mobile.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NavigationManager {
    private val _navController = MutableStateFlow<NavController?>(null)
    val navController: StateFlow<NavController?> = _navController.asStateFlow()

    fun setNavController(controller: NavController) {
        _navController.value = controller
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

    fun navigateUp() {
        _navController.value?.navigateUp()
    }

    fun navigateToRepoDetail(owner: String, name: String) {
        _navController.value?.navigate(AppDestinations.RepoDetail.createRoute(owner, name))
    }
} 