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

class TodoModel(application: Application) : AndroidViewModel(application) {

    private val repo: TodoRepo

    init {
        val db = TodoDatabase.getInstance(application)
        repo = TodoRepo(db.todoDao())
        loadAllTodos() // to load all todos to appear once app is running
    }

    // MutableStateFlow (List of objects) -> anyone listening to it is notified whenever the value changes
    // Compose will automatically recompose UI when the value changes
    // mutable (changeable) -> means ViewModel can change the value
    // _ -> means variable is private
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // read-only version of _todos (for ui to be able to get data but not modify it)
    // asStateFlow -> make a wrapper of _todos
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()
    private val _selectedTodo = MutableStateFlow<Todo?>(null)
    val selectedTodo = _selectedTodo.asStateFlow()
    private fun loadAllTodos() {
        viewModelScope.launch {
            // to load all todos on _todos
            repo.getAllTodos().collect { list -> _todos.value = list }
        }
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

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repo.deleteTodo(todo)
        }
    }

    suspend fun getToDoById(id:Int):Todo ?{

            return repo.getTodoById(id)
    }

     fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repo.updateTodo(todo)
        }
    }
    fun loadTodoById(id: Int) {
        viewModelScope.launch {
            val todo = repo.getTodoById(id)
            _selectedTodo.value = todo
        }
    }
  }