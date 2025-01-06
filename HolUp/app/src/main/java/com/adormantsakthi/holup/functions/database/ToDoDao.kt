package com.adormantsakthi.holup.functions.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adormantsakthi.holup.functions.Todo

@Dao
interface ToDoDao {
    @Query("SELECT * FROM TODO")
    fun getAllTodo(): LiveData<List<Todo>>

    @Insert
    fun addTodo(todo: Todo)

    @Query("DELETE FROM Todo where id = :id")
    fun deleteTodo(id: Int)

    @Query("UPDATE TODO SET title = :new_title WHERE id = :id")
    fun editTodo(id: Int, new_title: String)

    @Query("SELECT * FROM TODO WHERE id = :id")
    fun getTodo(id: Int): LiveData<Todo>
}