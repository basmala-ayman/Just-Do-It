package com.todo.just_do_it

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.todo.just_do_it.model.TodoView
import com.todo.just_do_it.screen.TodoList

class MainActivity : ComponentActivity() {
    private val viewModel: TodoView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val todos by viewModel.todos.collectAsState()

            TodoList(
                todos = todos,
                onAddClick = { title, desc -> viewModel.addTodo(title, desc) },
                onDoneClick = { todo -> viewModel.toggleDone(todo) },
                onDeleteClick = { todo -> viewModel.deleteTodo(todo) }
            )
        }
    }
}









