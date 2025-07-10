package com.elisz11.todo.RoomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val taskTitle: String,
    val taskDesc: String,
    val taskPriority: Int,
    @PrimaryKey(autoGenerate = true)
    val taskId : Int = 0
)
