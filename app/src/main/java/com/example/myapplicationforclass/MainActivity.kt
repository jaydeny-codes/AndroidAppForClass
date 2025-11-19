package com.example.myapplicationforclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.ui.theme.MyApplicationForClassTheme
import kotlinx.coroutines.launch

data class TodoItem(val id: Long, val title: String, val done: Boolean = false)

// --------- New: simple screen enum ----------
enum class AppScreen {
    Home,
    Edit
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationForClassTheme {
                CatTodoApp()
            }
        }
    }
}

// --------- New: Root composable with hamburger menu + navigation ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatTodoApp() {
    // Shared list so BOTH screens see the same tasks
    val items = remember { mutableStateListOf<TodoItem>() }

    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("To-Do List") },
                    selected = currentScreen == AppScreen.Home,
                    onClick = {
                        currentScreen = AppScreen.Home
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.List, contentDescription = null)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Edit List") },
                    selected = currentScreen == AppScreen.Edit,
                    onClick = {
                        currentScreen = AppScreen.Edit
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (currentScreen == AppScreen.Home)
                                "Cat To-Do"
                            else
                                "Edit To-Do List"
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (currentScreen) {
                AppScreen.Home -> CatTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                AppScreen.Edit -> EditTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

// --------- Original screen, now using shared items list ----------
@Composable
fun CatTodoScreen(
    items: MutableList<TodoItem>,          // <— changed: passed in instead of created inside
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    Box(modifier = modifier) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.cat_background),
            contentDescription = "Cat background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Soft dark overlay so content is readable
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My To-Do List 🐾",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            // Input row
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
                        val trimmed = input.trim()
                        if (trimmed.isNotEmpty()) {
                            items.add(
                                0,
                                TodoItem(
                                    id = System.currentTimeMillis(),
                                    title = trimmed
                                )
                            )
                            input = ""
                            focus.clearFocus()
                        }
                    }
                ) { Text("Add") }
            }

            Spacer(Modifier.height(12.dp))

            // Empty state
            if (items.isEmpty()) {
                Text(
                    text = "Nothing yet. Add your first task!",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            // To-do list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    TodoRow(
                        item = item,
                        onCheckedChange = { checked ->
                            val idx = items.indexOfFirst { it.id == item.id }
                            if (idx != -1) {
                                items[idx] = items[idx].copy(done = checked)
                            }
                        },
                        onDelete = {
                            items.removeAll { it.id == item.id }
                        }
                    )
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

// --------- New: Edit screen ----------
@Composable
fun EditTodoScreen(
    items: MutableList<TodoItem>,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<TodoItem?>(null) }
    var editText by remember { mutableStateOf("") }

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

        if (items.isEmpty()) {
            Text(
                text = "No items to edit yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    Surface(
                        tonalElevation = 1.dp,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem = item
                                editText = item.title
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (item.done) {
                                Text(
                                    text = "Done",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Green
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Edit panel
        if (selectedItem != null) {
            Text(
                text = "Editing:",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = selectedItem!!.title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val trimmed = editText.trim()
                        if (trimmed.isNotEmpty()) {
                            val id = selectedItem!!.id
                            val idx = items.indexOfFirst { it.id == id }
                            if (idx != -1) {
                                val old = items[idx]
                                items[idx] = old.copy(title = trimmed)
                            }
                            selectedItem = null
                            editText = ""
                        }
                    }
                ) {
                    Text("Save")
                }

                OutlinedButton(
                    onClick = {
                        selectedItem = null
                        editText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Clear all button
        if (items.isNotEmpty()) {
            OutlinedButton(
                onClick = { items.clear() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Clear entire list")
            }
        }
    }
}

// --------- Existing row composable ----------
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
            Checkbox(
                checked = item.done,
                onCheckedChange = onCheckedChange
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = Color.Black,
                    textDecoration = if (item.done) TextDecoration.LineThrough else TextDecoration.None
                )
            )
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatTodoPreview() {
    MyApplicationForClassTheme {
        // For preview we can give an empty list
        CatTodoScreen(
            items = remember { mutableStateListOf() },
            modifier = Modifier.fillMaxSize()
        )
    }
}
