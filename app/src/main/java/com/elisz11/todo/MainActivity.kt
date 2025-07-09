package com.elisz11.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elisz11.todo.ui.theme.TodoTheme
import java.nio.file.WatchEvent
import androidx.compose.material3.CardColors
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val ToDo = listOf(
            ToDo("Kungico", "Ammazza Kungico", 3),
            ToDo("Kungico", "Ammazza Kungico", 2),
            ToDo("Kungico", "Ammazza Kungico", 1),
            ToDo("Kungico", "Ammazza Kungico", 0),
        )

        setContent {
            TodoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(ToDo) {
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

            /*Image(
                painter = painterResource(id = R.drawable.outline_assignment_24),
                contentDescription = "Photo of person",
                modifier = Modifier.width(100.dp)
                    .height(100.dp)
            )*/

            Column {

                Text(
                    text = todo.title,
                    modifier = Modifier.padding(10.dp)
                )

                Text(
                    text = todo.description,
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