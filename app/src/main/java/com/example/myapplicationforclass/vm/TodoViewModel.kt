package com.example.myapplicationforclass.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myapplicationforclass.data.TodoItem

// ViewModel stores and manages UI-related data in a lifecycle-conscious way.
// Here it owns the list of to-do items and exposes functions to modify it.
class TodoViewModel : ViewModel() {

    // Using mutableStateListOf so Compose can automatically observe changes
    // and recompose any UI that reads this list.
    val items = mutableStateListOf<TodoItem>()

    // Add a new task at the top of the list.
    fun addTask(text: String) {
        val trimmed = text.trim()
        if (trimmed.isNotEmpty()) {
            items.add(
                0,
                TodoItem(
                    id = System.currentTimeMillis(), // simple unique ID based on time
                    title = trimmed
                )
            )
        }
    }

    // Mark a task as done or not done based on its id.
    fun toggleDone(id: Long, isDone: Boolean) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            items[index] = items[index].copy(done = isDone)
        }
    }

    // Delete a task by id.
    fun deleteTask(id: Long) {
        items.removeAll { it.id == id }
    }

    // Edit an existing task’s title by id.
    fun editTask(id: Long, newText: String) {
        val trimmed = newText.trim()
        if (trimmed.isNotEmpty()) {
            val index = items.indexOfFirst { it.id == id }
            if (index != -1) {
                items[index] = items[index].copy(title = trimmed)
            }
        }
    }

    // Clear all tasks from the list.
    fun clearAll() {
        items.clear()
    }
}
