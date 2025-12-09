package com.todo.just_do_it.data

import android.util.Log
import androidx.compose.animation.core.rememberTransition
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class FirestoreSyncManager(private val todoDao: TodoDao) {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("todos")
    private var registration: ListenerRegistration? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun startListening() {
        if (registration != null) {
            Log.d("FirestoreSync", "Already listening")
            return
        }
        registration = collection.addSnapshotListener { snapshot, error ->
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
                            Log.d("FirestoreSync", "Upserted todo id=$id title=$title")
                        }

                        DocumentChange.Type.REMOVED -> {
                            todoDao.deleteTodo(todo)
                            Log.d("FirestoreSync", "Deleted todo id=$id")
                        }
                    }
                }
            }
//            CoroutineScope(Dispatchers.IO).launch {
//                val docs = snapshot.documents
//                for (doc in docs) {
//                    val id = doc.id
//                    val title = doc.getString("title") ?: continue
//                    val description = doc.getString("description")
//                    val isDone = doc.getBoolean("isDone") ?: false
//                    val time = doc.getLong("time") ?: System.currentTimeMillis()
//                    val todo = Todo(
//                        id = id,
//                        title = title,
//                        description = description,
//                        isDone = isDone,
//                        time = time
//                    )
//                    todoDao.insertTodo(todo)
//                }
//            }
        }
        Log.d("FirestoreSync", "Started listening to Firestore 'todos'")

    }

    fun stopListening() {
        registration?.remove()
        registration = null
        scope.cancel()
        Log.d("FirestoreSync", "Stopped listening")
    }
}
