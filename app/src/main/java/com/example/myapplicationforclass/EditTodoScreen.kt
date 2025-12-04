package com.example.myapplicationforclass

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.data.TodoItem
import com.example.myapplicationforclass.vm.TodoViewModel

// Screen for editing existing tasks and clearing the whole list.
@Composable
fun EditTodoScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    // Observe list from ViewModel.
    val items = viewModel.items

    // Which item is currently selected for editing.
    var selectedItem by remember { mutableStateOf<TodoItem?>(null) }

    // Text being edited.
    var editText by remember { mutableStateOf("") }

    // Whether to show the "clear all" confirmation dialog.
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Tap an item to edit it",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(16.dp))

        // If list is empty, show a message instead of list.
        if (items.isEmpty()) {
            Text("No items to edit yet.")
        } else {
            // Scrollable list of tasks to pick from.
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // take remaining height
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    val isSelected = selectedItem?.id == item.id

                    Surface(
                        tonalElevation = if (isSelected) 4.dp else 1.dp,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // When tapped, mark as selected and pre-fill editText.
                                selectedItem = item
                                editText = item.title
                            }
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
                            if (item.done) {
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

        Spacer(Modifier.height(16.dp))

        // If an item is selected, show editing UI.
        selectedItem?.let { item ->
            Text("Editing:", style = MaterialTheme.typography.labelLarge)
            Text(
                item.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = editText,
                onValueChange = { editText = it },
                label = { Text("New text") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Save: use ViewModel to update the task.
                Button(onClick = {
                    viewModel.editTask(item.id, editText)
                    selectedItem = null
                    editText = ""
                }) {
                    Text("Save")
                }

                // Cancel editing.
                OutlinedButton(onClick = {
                    selectedItem = null
                    editText = ""
                }) {
                    Text("Cancel")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Button to clear the entire list (asks for confirmation).
        if (items.isNotEmpty()) {
            OutlinedButton(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear entire list")
            }
        }
    }

    // Confirmation dialog for clearing all tasks.
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all tasks?") },
            text = { Text("This will remove every item from your list.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAll()
                        showClearDialog = false
                    }
                ) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
