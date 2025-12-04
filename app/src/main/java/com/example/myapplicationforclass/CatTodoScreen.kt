package com.example.myapplicationforclass

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.data.TodoItem
import com.example.myapplicationforclass.vm.TodoViewModel

// Main screen: add tasks, show counts, and list all tasks.
@Composable
fun CatTodoScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    // Observe the list from the ViewModel.
    val items = viewModel.items

    // Local state for the text field input.
    var input by remember { mutableStateOf("") }

    // Used to clear keyboard focus after adding a task.
    val focus = LocalFocusManager.current

    // Derived stats from the list.
    val total = items.size
    val doneCount = items.count { it.done }
    val remaining = total - doneCount

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title at the top of the screen.
        Text(
            text = "My To-Do List 🐾",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        // Summary card with total / done / left count.
        Surface(
            tonalElevation = 3.dp,
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total: $total")
                Text("Done: $doneCount")
                Text("Left: $remaining")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Input row: text field for new task + Add button.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Add a task…") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = {
                    // Call ViewModel to add a new task.
                    viewModel.addTask(input)
                    input = ""
                    focus.clearFocus()
                }
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        // If there are no tasks, show a hint.
        if (items.isEmpty()) {
            Text("Nothing yet. Add your first task!")
        } else {
            // Show a scrollable list of tasks.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // items(...) iterates through TodoItem entries.
                items(
                    items = items,
                    key = { todo -> todo.id } // stable key based on id
                ) { item ->
                    // Use a separate composable for each row.
                    TodoRow(
                        item = item,
                        onCheckedChange = { checked ->
                            viewModel.toggleDone(item.id, checked)
                        },
                        onDelete = { viewModel.deleteTask(item.id) }
                    )
                }

                // Spacer at bottom of list.
                item {
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

// Composable that renders a single row in the list:
// Checkbox + title + delete icon.
@Composable
fun TodoRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox to mark done/undone.
            Checkbox(
                checked = item.done,
                onCheckedChange = onCheckedChange
            )

            Spacer(Modifier.width(8.dp))

            // Task title, with strikethrough if completed.
            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (item.done) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
            )

            // Delete icon button to remove the task.
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}
