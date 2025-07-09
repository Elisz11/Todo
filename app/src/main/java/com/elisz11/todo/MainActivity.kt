package com.elisz11.todo

import com.elisz11.todo.ui.theme.TodoTheme
import com.elisz11.todo.data.ToDo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateListOf

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tasks = mutableStateListOf<ToDo>().apply {
            addAll(listOf(
                ToDo("Kungico", "Ammazza Kungico", 3),
                ToDo("Kungico", "Ammazza Kungico", 2)
            ))
        }

        setContent {
            TodoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("ToDo List") }
                        )
                    },
                    floatingActionButton = {
                        AddButton(onClick = {
                            val newPriority = (0..3).random()
                            val newItem = ToDo("Nuova Task Dinamica", "Aggiunta al volo!", newPriority)
                            tasks.add(newItem)
                            android.util.Log.d("FAB_CLICK", "Aggiunta nuova task: $newItem. Numero di task: ${tasks.size}")
                        })
                    }
                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(tasks.sortedByDescending { it.priority }) {
                            CardView(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardView(todo: ToDo) {
    Card(colors = priorityColor(todo.priority), modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row {
            Column {
                Text(
                    text = todo.title,
                    modifier = Modifier.padding(10.dp)
                )

                Text(
                    text = todo.desc,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun priorityColor(priority: Int): CardColors {
    return when (priority) {
        3 -> CardDefaults.cardColors(containerColor = Color(0xFFE57373))
        2 -> CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B))
        1 -> CardDefaults.cardColors(containerColor = Color(0xFFA5D6A7))
        0 -> CardDefaults.cardColors(containerColor = Color.LightGray)
        else -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Add new ToDo item.")
    }
}