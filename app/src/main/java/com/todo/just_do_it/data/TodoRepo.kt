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
        // insert on room db
        dao.insertTodo(todo)
        // insert on firestore db
        try {
            firestore.saveTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        // update on room db
        dao.updateTodo(todo)
        // update on firestore db
        try {
            firestore.saveTodo(todo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTodo(todo: Todo) {
        // delete on room db
        dao.deleteTodo(todo)
        // delete on firestore db
        try {
            firestore.deleteTodoById(todo.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}