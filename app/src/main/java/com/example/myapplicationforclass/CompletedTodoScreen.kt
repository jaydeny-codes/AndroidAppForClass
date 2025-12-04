package com.example.myapplicationforclass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.vm.TodoViewModel

// Screen that shows only tasks with done == true.
@Composable
fun CompletedTodoScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    // Filter the list to only completed items.
    val completed = viewModel.items.filter { it.done }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Completed Tasks",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        if (completed.isEmpty()) {
            // Message when no tasks are completed yet.
            Text("You haven't completed any tasks yet.")
        } else {
            // List completed tasks in simple rows.
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                completed.forEach { item ->
                    Surface(
                        tonalElevation = 2.dp,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Done",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
