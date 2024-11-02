import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cpy3f2.gixor_mobile.ui.screens.LoginScreen
import com.cpy3f2.gixor_mobile.ui.screens.MainFrame
import com.cpy3f2.gixor_mobile.ui.screens.SearchScreen
import com.cpy3f2.gixor_mobile.viewModel.MainViewModel

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
    }
} 