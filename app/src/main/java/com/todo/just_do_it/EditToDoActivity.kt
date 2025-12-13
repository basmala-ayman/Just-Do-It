package com.todo.just_do_it

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.todo.just_do_it.data.Todo
import com.todo.just_do_it.model.TodoModel
import kotlinx.coroutines.launch
import com.todo.just_do_it.screen.EditToDoScreen
import com.todo.just_do_it.ui.theme.JustDoItTheme
import com.todo.just_do_it.util.SettingsStore
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class EditToDoActivity : ComponentActivity() {
    private val todoModel: TodoModel by viewModels()
    private var selectedTodo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val settingsStore = SettingsStore(applicationContext)
        super.onCreate(savedInstanceState)
        val todoId = intent.getStringExtra("id")
        if (todoId == null) {
            finish()
            return
        }

        lifecycleScope.launch {

            selectedTodo = todoModel.getToDoById(todoId)

            setContent {
                // 2. Listen
                val isDark by settingsStore.isDarkMode.collectAsState(initial = false)
                val themeColor by settingsStore.themeColor.collectAsState(initial = "Pink")

                // 3. Apply Theme
                JustDoItTheme(darkTheme = isDark, themeColor = themeColor) {
                    EditToDoScreen(
                        todo = selectedTodo,
                        onSave = { updatedTodo ->
                            todoModel.updateTodo(updatedTodo)
                            finish()
                        },
                        onBack = { finish() } // Make sure to add this parameter to EditToDoScreen if you haven't!
                    )
                }
            }
        }
    }
}