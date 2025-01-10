package com.adormantsakthi.holup.ui.Todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adormantsakthi.holup.functions.Todo
import com.adormantsakthi.holup.functions.database.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class TodoViewModel: ViewModel() {

    val todoDao = MainApplication.todoDatabase.getTodoDao()

    val todoList : LiveData<List<Todo>> = todoDao.getAllTodo()
    val remainingTodoList: LiveData<List<Todo>> = todoDao.getRemainingTodo()

    val totalNumOfTodos: LiveData<Float> = todoDao.getTotalNumOfTodo()
    val totalNumOfCompletedTodos: LiveData<Float> = todoDao.getCompletedNumOfTodo()

    fun addTodo(title: String){
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(Todo(title = title, isCompleted = false, importance = "low", createdAt = Date.from(Instant.now())))
        }
    }

    fun deleteTodo(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id)
        }
    }

    fun editTodo(id: Int, new_title: String, isCompleted: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.editTodo(id, new_title, isCompleted)
        }
    }
}