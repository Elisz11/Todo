package com.elisz11.todo

import com.elisz11.todo.RoomDb.Task
import com.elisz11.todo.RoomDb.TaskDatabase
import com.elisz11.todo.ui.theme.TodoTheme
import com.elisz11.todo.viewModel.TaskViewModel
import com.elisz11.todo.viewModel.Repository

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            name = "tasks.db"
        ).build()
    }
    private val viewModel by viewModels<TaskViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(Repository(db)) as T
                }
            }
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoTheme {

                var showAddEditTaskDialog by remember { mutableStateOf(false) }

                var name by remember {
                    mutableStateOf("")
                }
                var desc by remember {
                    mutableStateOf("")
                }
                val task = Task(
                    name,
                    desc,
                    0
                )
                var taskList by remember {
                    mutableStateOf(listOf<Task>())
                }
                viewModel.getTasks().observe(this){
                    taskList = it
                }

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(title = {
                        Text("ToDo List")
                    }) },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showAddEditTaskDialog = true },
                        ) {
                            Icon(Icons.Filled.Add, "Add new ToDo item.")
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(taskList.sortedByDescending { it.taskPriority }) { task->
                            CardView(task) { clicked ->
                                viewModel.deleteTask(task)
                            }
                        }
                    }
                }
                if (showAddEditTaskDialog) {
                    AddEditTaskDialog(
                        onDismiss = { showAddEditTaskDialog = false },
                        onConfirm = { task ->
                            viewModel.upsertTask(task)
                            showAddEditTaskDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CardView(task: Task, onClick: (Task) -> Unit) {
    Card(
        colors = priorityColor(task.taskPriority), modifier = Modifier.fillMaxSize().padding(12.dp).clickable { onClick(task) }
    ) {
        Row {
            Column {
                Text(
                    text = task.taskTitle,
                    modifier = Modifier.padding(10.dp),
                    color = Color.Black
                )

                Text(
                    text = task.taskDesc,
                    modifier = Modifier.padding(10.dp),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun priorityColor(priority: Int): CardColors {
    return when (priority) {
        3 -> CardDefaults.cardColors(containerColor = Color.Red)
        2 -> CardDefaults.cardColors(containerColor = Color.Yellow)
        1 -> CardDefaults.cardColors(containerColor = Color.Green)
        0 -> CardDefaults.cardColors(containerColor = Color.LightGray)
        else -> CardDefaults.cardColors(containerColor = Color.LightGray)
    }
}
@Composable
fun AddEditTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var taskPriority by remember { mutableIntStateOf(0) }
    var taskPriorityText by remember { mutableStateOf(taskPriority.toString()) }
    var isTitleError by remember { mutableStateOf(false) }
    var isPriorityError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a new Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = {
                        taskTitle = it
                        isTitleError = it.isBlank()
                    },
                    label = { Text(" Task title") },
                    singleLine = true,
                    isError = isTitleError,
                    supportingText = {
                        if (isTitleError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Title is required",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = taskDesc,
                    onValueChange = { taskDesc = it },
                    label = { Text("Task description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = taskPriorityText,
                    onValueChange = { newValue ->
                        taskPriorityText = newValue

                        val intValue = newValue.toIntOrNull()
                        if (intValue != null) {
                            if (intValue in 0..3) {
                                taskPriority = intValue
                                isPriorityError = false
                            } else {
                                isPriorityError = true
                            }
                        } else if (newValue.isEmpty()) {
                            taskPriority = 0
                            isPriorityError = false
                        } else {
                            isPriorityError = true
                        }
                    },
                    label = { Text("Priority (0-3)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = isPriorityError,
                    supportingText = {
                        if (isPriorityError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Priority must be a number between 0 and 3",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val titleValid = taskTitle.isNotBlank()
                    val priorityValid = taskPriorityText.toIntOrNull()?.let { it in 0..3 } ?: false

                    isTitleError = !titleValid
                    isPriorityError = !priorityValid

                    if (titleValid && priorityValid) {
                        val newTask = Task(
                            taskTitle = taskTitle.trim(),
                            taskDesc = taskDesc.trim(),
                            taskPriority = taskPriority
                        )
                        onConfirm(newTask)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}