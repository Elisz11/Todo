package com.elisz11.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.elisz11.todo.RoomDb.Task
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: Repository): ViewModel() {
    fun getTasks() = repository.getAllTasks().asLiveData(viewModelScope.coroutineContext)

    fun upsertTask(task: Task){
        viewModelScope.launch {
            repository.upsertTask(task)
        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}