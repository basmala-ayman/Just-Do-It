package com.todo.just_do_it.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- COLOR DEFINITIONS ---
// Pink (Default)
val PinkPrimary = Color(0xFFFF8FA3)
val PinkSecondary = Color(0xFF80D8C8)

// Blue (Ocean)
val BluePrimary = Color(0xFF42A5F5)
val BlueSecondary = Color(0xFF90CAF9)

// Green (Nature)
val GreenPrimary = Color(0xFF66BB6A)
val GreenSecondary = Color(0xFFA5D6A7)

// Dark / Light Backgrounds
val DarkBackground = Color(0xFF1C1B1F)
val LightBackground = Color(0xFFFFF8F0)

@Composable
fun JustDoItTheme(
    // These inputs control the theme now!
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: String = "Pink",
    content: @Composable () -> Unit
) {
    // 1. Choose the Primary Color based on the name
    val (primary, secondary) = when (themeColor) {
        "Blue" -> Pair(BluePrimary, BlueSecondary)
        "Green" -> Pair(GreenPrimary, GreenSecondary)
        else -> Pair(PinkPrimary, PinkSecondary) // Default Pink
    }

    // 2. Create the Color Scheme
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primary,
            secondary = secondary,
            background = DarkBackground,
            surface = Color(0xFF303030), // Dark Cards
            onPrimary = Color.White
        )
    } else {
        lightColorScheme(
            primary = primary,
            secondary = secondary,
            background = LightBackground,
            surface = Color.White,
            onPrimary = Color.White
        )
    }

    // 3. Fix Status Bar Color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}