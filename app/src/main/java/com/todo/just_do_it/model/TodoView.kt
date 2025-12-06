package com.todo.just_do_it.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.todo.just_do_it.data.Todo
import com.todo.just_do_it.data.TodoDatabase
import com.todo.just_do_it.data.TodoRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.security.auth.Destroyable

class TodoView(application: Application) : AndroidViewModel(application) {

    private val repo: TodoRepo

    init {
        val db = TodoDatabase.getInstance(application)
        repo = TodoRepo(db.todoDao())
        observeTodos() // to load all todos
    }

    // MutableStateFlow (List of objects) -> anyone listening to it is notified whenever the value changes
    // Compose will automatically recompose UI when the value changes
    // mutable (changeable) -> means ViewModel can change the value
    // _ -> means variable is private
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // read-only version of _todos (for ui to be able to get data but not modify it)
    // asStateFlow -> make a wrapper of _todos
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private fun observeTodos() {
        viewModelScope.launch {
            // to load all todos on _todos
            repo.getAllTodos().collect { list -> _todos.value = list }
        }
    }

    // for a specific todo -> when user click on it
    private val _selectedTodo = MutableStateFlow<Todo?>(null)
    val selectedTodo: StateFlow<Todo?> = _selectedTodo.asStateFlow()

    fun showSpecificTodo(id: Int) {
        viewModelScope.launch {
            val todo = try {
                repo.getTodoById(id)
            } catch (e: Exception) {
                null
            }
            _selectedTodo.value = todo
        }
    }

    fun clearSelected() {
        _selectedTodo.value = null
    }

    fun addTodo(title: String, description: String? = null) {
        // isBlank() -> true -> "", " ", " \n "
        if (title.isBlank())
            return
        viewModelScope.launch {
            repo.insertTodo(title, description)
        }
    }

    fun toggleDone(todo: Todo) {
        viewModelScope.launch {
            // make a copy from the same todo with opposite done status
            val toggledTodo = todo.copy(isDone = !todo.isDone)
            repo.updateTodo(toggledTodo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repo.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repo.deleteTodo(todo)
        }
    }
}