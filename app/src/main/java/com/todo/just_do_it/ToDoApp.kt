package com.todo.just_do_it

import android.app.Application
import com.todo.just_do_it.data.FirestoreSyncManager
import com.todo.just_do_it.data.TodoDatabase

class ToDoApp : Application() {
    private lateinit var syncManager: FirestoreSyncManager

    override fun onCreate() {
        super.onCreate()

        val db = TodoDatabase.getInstance(this)
        syncManager = FirestoreSyncManager(db.todoDao())
        syncManager.startListening()
    }

    override fun onTerminate() {
        syncManager.stopListening()
        super.onTerminate()
    }
}