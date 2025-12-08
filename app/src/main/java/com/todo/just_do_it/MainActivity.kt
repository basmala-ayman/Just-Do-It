package com.todo.just_do_it

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.todo.just_do_it.model.TodoView
import com.todo.just_do_it.screen.TodoList
import com.todo.just_do_it.ui.theme.JustDoItTheme // <--- IMPORT THIS!

class MainActivity : ComponentActivity() {

    private val viewModel: TodoView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // --- CHANGE IS HERE ---
            // We wrap everything inside our custom Theme
            JustDoItTheme {

                // Collect the data just like before
                val todos by viewModel.todos.collectAsState()

                // Show the screen
                TodoList(
                    todos = todos,
                    onAddClick = { title, desc -> viewModel.addTodo(title, desc) },
                    onDoneClick = { todo -> viewModel.toggleDone(todo) },
                    onDeleteClick = { todo -> viewModel.deleteTodo(todo) }
                )
            }
        }
    }
}