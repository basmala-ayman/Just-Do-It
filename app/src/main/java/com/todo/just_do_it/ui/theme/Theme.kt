package com.todo.just_do_it.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Define the Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPink,        // We keep the Pink pop!
    secondary = SecondaryMint,    // We keep the Mint!
    tertiary = AccentPurple,
    background = BackgroundDark,  // Dark background
    surface = SurfaceDark,        // Dark cards
    onPrimary = Color.White,
    onSecondary = BackgroundDark,
    onTertiary = BackgroundDark,
    onBackground = TextLight,     // White text
    onSurface = TextLight         // White text on cards
)

// 2. Define the Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryPink,
    secondary = SecondaryMint,
    tertiary = AccentPurple,
    background = BackgroundCream,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSecondary = TextDark,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun JustDoItTheme(
    // Check if the system is in Dark Mode
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We keep dynamicColor FALSE to ensure our Pink/Mint is always used
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // 3. Pick the right scheme based on the boolean above
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // 4. Update the Status Bar color (Top of phone)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Match background

            // If it's light mode, make icons dark. If dark mode, make icons light.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure Type.kt is correct, or use MaterialTheme.typography
        content = content
    )
}