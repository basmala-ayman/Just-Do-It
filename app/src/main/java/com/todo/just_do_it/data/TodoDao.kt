package com.todo.just_do_it.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // get all to do
    @Query("SELECT * FROM todos ORDER BY time DESC")
    fun getAllTodos(): Flow<List<Todo>>

    // get single to do by id
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    // insert a new to do
    @Insert
    suspend fun insertTodo(todo: Todo)

    // edit note
    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}