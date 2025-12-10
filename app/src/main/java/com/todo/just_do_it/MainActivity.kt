package com.todo.just_do_it

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.todo.just_do_it.model.TodoModel
import com.todo.just_do_it.screen.TodoList
import com.todo.just_do_it.ui.theme.JustDoItTheme
import com.todo.just_do_it.util.SettingsStore

class MainActivity : ComponentActivity() {
    private val viewModel: TodoModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsStore = SettingsStore(applicationContext)
        setContent {
            val isDark by settingsStore.isDarkMode.collectAsState(initial = false)
            val themeColor by settingsStore.themeColor.collectAsState(initial = "Pink")

            JustDoItTheme(darkTheme = isDark, themeColor = themeColor) {

                val todos by viewModel.todos.collectAsState()
                TodoList(
                    todos = todos,
                    onAddClick = { title, desc -> viewModel.addTodo(title, desc) },
                    onDoneClick = { todo -> viewModel.toggleDone(todo) },
                    onDeleteClick = { todo -> viewModel.deleteTodo(todo) },
                    onClickToDO = { id: String ->
                        val intent = Intent(this, EditToDoActivity::class.java)
                        intent.putExtra("id", id)
                        startActivity(intent)
                    },
                    onSettingsClick = {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}









