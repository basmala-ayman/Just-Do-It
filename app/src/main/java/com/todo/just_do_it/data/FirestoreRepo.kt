package com.todo.just_do_it.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepo {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("todos")

    suspend fun saveTodo(todo: Todo) {
        val map = hashMapOf(
            "id" to todo.id,
            "title" to todo.title,
            "description" to todo.description,
            "isDone" to todo.isDone,
            "time" to todo.time
        )
        collection.document(todo.id).set(map).await()
    }

    suspend fun deleteTodoById(id: String) {
        collection.document(id).delete().await()
    }

    suspend fun loadAllTodos(): List<Todo> {
        val snap = collection.get().await()
        return snap.documents.mapNotNull { doc ->
            val id = doc.id
            val title = doc.getString("title") ?: return@mapNotNull null
            val description = doc.getString("description")
            val isDone = doc.getBoolean("isDone") ?: false
            val time = doc.getLong("time") ?: System.currentTimeMillis()
            Todo(id = id, title = title, description = description, isDone = isDone, time = time)
        }
    }

}