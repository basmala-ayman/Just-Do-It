package com.todo.just_do_it.data

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FirestoreSyncManager(private val todoDao: TodoDao) {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("todos")
    private var listener: ListenerRegistration? = null // firebase listener

    // if one failed -> not stop for all sync
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun startListening() {
        if (listener != null) {
            Log.d("FirestoreSync", "Already listening")
            return
        }
        // once change happens
        listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FirestoreSync", "Error in listener", error)
                return@addSnapshotListener
            }
            if (snapshot == null) {
                Log.w("FirestoreSync", "Snapshot is null")
                return@addSnapshotListener
            }

            scope.launch {
                for (c in snapshot.documentChanges) {
                    val doc = c.document

                    val id = doc.id
                    val title = doc.getString("title") ?: continue
                    val description = doc.getString("description")
                    val isDone = doc.getBoolean("isDone") ?: false
                    val time = doc.getLong("time") ?: System.currentTimeMillis()

                    val todo = Todo(
                        id = id,
                        title = title,
                        description = description,
                        isDone = isDone,
                        time = time
                    )

                    when (c.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED -> {
                            todoDao.insertTodo(todo)
                        }

                        DocumentChange.Type.REMOVED -> {
                            todoDao.deleteTodo(todo)
                        }
                    }
                }
            }
        }
    }

    fun stopListening() {
        listener?.remove()
        listener = null
        scope.cancel()
    }
}
