import android.net.Uri

sealed class AppDestinations(val route: String) {
    object Login : AppDestinations("login")
    object Main : AppDestinations("main")
    object Search : AppDestinations("search")
    object Message : AppDestinations("message")
    object Chat : AppDestinations("chat/{userId}/{userName}") {
        fun createRoute(userId: String, userName: String): String {
            return "chat/${Uri.encode(userId)}/${Uri.encode(userName)}"
        }
    }
    object NotificationDetail : AppDestinations("notification/{notificationId}?repository={repository}") {
        fun createRoute(notificationId: String, repository: String? = null): String {
            return "notification/${Uri.encode(notificationId)}?repository=${Uri.encode(repository ?: "")}"
        }
    }
    object RepoDetail : AppDestinations("repo_detail/{owner}/{name}") {
        fun createRoute(owner: String, name: String): String {
            return "repo_detail/$owner/$name"
        }
    }
    
    companion object {
        fun fromRoute(route: String): AppDestinations {
            return when(route) {
                "login" -> Login
                "main" -> Main
                "search" -> Search
                "message" -> Message
                "chat" -> Chat
                "notification" -> NotificationDetail
                "repo_detail" -> RepoDetail
                else -> Main
            }
        }
    }
} 