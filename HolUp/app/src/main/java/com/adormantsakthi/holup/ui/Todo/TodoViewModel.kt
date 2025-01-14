package com.adormantsakthi.holup.ui.Todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adormantsakthi.holup.functions.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Calendar
import java.util.Date

class TodoViewModel: ViewModel() {

    private val todoDao = MainApplication.todoDatabase.getTodoDao()

        private val midnightMillis: Long

        init {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0) // Set hour to 12 AM
            calendar.set(Calendar.MINUTE, 0)     // Set minutes to 0
            calendar.set(Calendar.SECOND, 0)     // Set seconds to 0
            calendar.set(Calendar.MILLISECOND, 0) // Set milliseconds to 0

            midnightMillis = calendar.timeInMillis // Get the time in milliseconds
        }


    val todoList : LiveData<List<Todo>> = todoDao.getAllTodo(midnightMillis)
    val remainingTodoList: LiveData<List<Todo>> = todoDao.getRemainingTodo(midnightMillis)

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