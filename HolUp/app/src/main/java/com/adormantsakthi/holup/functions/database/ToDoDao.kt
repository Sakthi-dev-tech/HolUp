package com.adormantsakthi.holup.functions.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adormantsakthi.holup.functions.Todo

@Dao
interface ToDoDao {
    @Query("SELECT * FROM TODO WHERE createdAt >= :midnightMillis")
    fun getAllTodo(midnightMillis: Long): LiveData<List<Todo>>

    @Insert
    fun addTodo(todo: Todo)

    @Query("DELETE FROM Todo where id = :id")
    fun deleteTodo(id: Int)

    @Query("UPDATE TODO SET title = :new_title, isCompleted = :isCompleted WHERE id = :id")
    fun editTodo(id: Int, new_title: String, isCompleted: Boolean)

    @Query("SELECT * FROM TODO WHERE id = :id")
    fun getTodo(id: Int): LiveData<Todo>

    @Query("SELECT * FROM TODO WHERE isCompleted = 0 AND createdAt >= :midnightMillis")
    fun getRemainingTodo(midnightMillis: Long): LiveData<List<Todo>>

    @Query("SELECT COUNT(*) AS total_count FROM TODO")
    fun getTotalNumOfTodo(): LiveData<Float>

    @Query("SELECT COUNT(*) AS completed_count FROM TODO WHERE isCompleted = 1")
    fun getCompletedNumOfTodo(): LiveData<Float>
}