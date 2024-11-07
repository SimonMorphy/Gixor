package com.cpy3f2.gixor_mobile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Primary,
    tertiary = Primary,
    background = Background_Dark,
    surface = Surface_Dark,
    onPrimary = Text_Primary_Dark,
    onSecondary = Text_Primary_Dark,
    onTertiary = Text_Primary_Dark,
    onBackground = Text_Primary_Dark,
    onSurface = Text_Primary_Dark,
    secondaryContainer = Surface_Dark,
    onSecondaryContainer = Text_Secondary_Dark,
    surfaceTint = Loading_Dark,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Primary,
    tertiary = Primary,
    background = Background_Light,
    surface = Surface_Light,
    onPrimary = Text_Primary_Light,
    onSecondary = Text_Primary_Light,
    onTertiary = Text_Primary_Light,
    onBackground = Text_Primary_Light,
    onSurface = Text_Primary_Light,
    secondaryContainer = Surface_Light,
    onSecondaryContainer = Text_Secondary_Light,
    surfaceTint = Loading_Light,
)

@Composable
fun GixorMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val context = LocalContext.current
    
    // 设置系统栏颜色
    if (context is Activity) {
        SideEffect {
            with(context.window) {
                // 状态栏和导航栏背景色
                val window = (context as Activity).window
                window.statusBarColor = if (darkTheme) Background_Dark.toArgb() else Background_Light.toArgb()
                window.navigationBarColor = if (darkTheme) Background_Dark.toArgb() else Background_Light.toArgb()
                
                // 状态栏和导航栏文字/图标颜色
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
