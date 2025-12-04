package com.example.myapplicationforclass.data

// Simple data model representing a single to-do item.
// - id: unique ID for the item (used for keys, editing, etc.)
// - title: the task text
// - done: whether the task is completed or not (defaults to false)
data class TodoItem(
    val id: Long,
    val title: String,
    val done: Boolean = false
)
