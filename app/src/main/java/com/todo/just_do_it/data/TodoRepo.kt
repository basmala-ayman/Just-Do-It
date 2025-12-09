package com.todo.just_do_it.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TodoRepo(
    private val dao: TodoDao,
    private val firestore: FirestoreRepo
) {

    fun getAllTodos(): Flow<List<Todo>> = dao.getAllTodos()

    suspend fun getTodoById(id: String): Todo? = dao.getTodoById(id)

    suspend fun insertTodo(title: String, description: String? = null) {
        val todo = Todo(id = UUID.randomUUID().toString(), title = title, description = description)
        dao.insertTodo(todo)
        try {
            firestore.saveTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        dao.updateTodo(todo)
        try {
            firestore.saveTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
        try {
            firestore.deleteTodoById(todo.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // ensure local deleted as well
//        dao.deleteTodo(todo)
    }

}