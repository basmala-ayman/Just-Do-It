package com.todo.just_do_it

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.todo.just_do_it.screen.SettingsScreen
import com.todo.just_do_it.ui.theme.JustDoItTheme
import com.todo.just_do_it.util.SettingsStore
import androidx.compose.runtime.collectAsState

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsStore = SettingsStore(applicationContext)

        setContent {
            val isDark by settingsStore.isDarkMode.collectAsState(initial = false)
            val colorTheme by settingsStore.themeColor.collectAsState(initial = "Pink")

            JustDoItTheme(darkTheme = isDark, themeColor = colorTheme) {
                SettingsScreen(
                    settingsStore = settingsStore,
                    onBack = { finish() }
                )
            }
        }
    }
}