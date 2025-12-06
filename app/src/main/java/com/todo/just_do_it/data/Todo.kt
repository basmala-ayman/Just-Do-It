package com.todo.just_do_it.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false,
    val time: Long = System.currentTimeMillis()
)