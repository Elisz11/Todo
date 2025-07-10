package com.elisz11.todo.viewModel

import com.elisz11.todo.RoomDb.Task
import com.elisz11.todo.RoomDb.TaskDatabase

class Repository(private val db : TaskDatabase) {

    suspend fun upsertTask(task: Task){
        db.dao.upsertTask(task)
    }

    suspend fun deleteTask(task: Task){
        db.dao.deleteTask(task)
    }

    fun getAllTasks() = db.dao.getAllTasks()
}