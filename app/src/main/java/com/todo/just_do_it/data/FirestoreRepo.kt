package com.todo.just_do_it.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepo {
    private val db = FirebaseFirestore.getInstance()

    // to get our database from firestore
    private val collection = db.collection("todos")

    // save todos on firestore with its format (doc format)
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

}