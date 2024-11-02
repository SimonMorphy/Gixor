sealed class AppDestinations(val route: String) {
    object Login : AppDestinations("login")
    object Main : AppDestinations("main")
    object Search : AppDestinations("search")
    object Message : AppDestinations("message")
    
    companion object {
        fun fromRoute(route: String): AppDestinations {
            return when(route) {
                "login" -> Login
                "main" -> Main
                "search" -> Search
                "message" -> Message
                else -> Main
            }
        }
    }
} 