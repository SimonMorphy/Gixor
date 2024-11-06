import android.net.Uri

sealed class AppDestinations(val route: String) {
    object Splash : AppDestinations("splash")
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
    object UserProfile : AppDestinations("user_profile/{username}") {
        fun createRoute(username: String): String {
            return "user_profile/${Uri.encode(username)}"
        }
    }
    object IssueDetail : AppDestinations("issue_detail/{owner}/{repo}/{issueNumber}") {
        fun createRoute(owner: String, repo: String, issueNumber: Int): String {
            return "issue_detail/$owner/$repo/$issueNumber"
        }
    }
    object PullRequestDetail : AppDestinations("pr_detail/{owner}/{repo}/{number}") {
        fun createRoute(owner: String, repo: String, number: Long): String {
            return "pr_detail/$owner/$repo/$number"
        }
    }
    object CreateIssue : AppDestinations("create_issue/{owner}/{repo}") {
        fun createRoute(owner: String, repo: String): String {
            return "create_issue/$owner/$repo"
        }
    }
    
    companion object {
        fun fromRoute(route: String): AppDestinations {
            return when(route) {
                "splash" -> Splash
                "login" -> Login
                "main" -> Main
                "search" -> Search
                "message" -> Message
                "chat" -> Chat
                "notification" -> NotificationDetail
                "repo_detail" -> RepoDetail
                "user_profile" -> UserProfile
                "issue_detail" -> IssueDetail
                "pr_detail" -> PullRequestDetail
                "create_issue" -> CreateIssue
                else -> Main
            }
        }
    }
} 