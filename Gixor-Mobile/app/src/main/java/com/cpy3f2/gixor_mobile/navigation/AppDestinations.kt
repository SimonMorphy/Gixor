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
    
    companion object {
        fun fromRoute(route: String): AppDestinations {
            return when(route) {
                "login" -> Login
                "main" -> Main
                "search" -> Search
                "message" -> Message
                "chat" -> Chat
                else -> Main
            }
        }
    }
} 