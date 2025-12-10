package com.todo.just_do_it.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Create the DataStore (The File)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsStore(private val context: Context) {

    // 2. Define the Keys (The Labels)
    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val THEME_COLOR = stringPreferencesKey("theme_color") // "Pink", "Blue", "Green"
    }

    // 3. Get the Data (Reading)
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false // Default to Light Mode
        }

    val themeColor: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_COLOR] ?: "Pink" // Default to Pink
        }

    // 4. Save the Data (Writing)
    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
        }
    }

    suspend fun saveThemeColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_COLOR] = color
        }
    }
}