package com.todo.just_do_it.data

import kotlinx.coroutines.flow.Flow

class TodoRepo(private val dao: TodoDao) {

    fun getAllTodos(): Flow<List<Todo>> = dao.getAllTodos()

    suspend fun getTodoById(id: Int): Todo?= dao.getTodoById(id)

    suspend fun insertTodo(title: String, description: String? = null){
        val todo = Todo(title = title, description = description)
        dao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) = dao.updateTodo(todo)

    suspend fun deleteTodo(todo: Todo) = dao.deleteTodo(todo)

}