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
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.ui.theme.MyApplicationForClassTheme
import kotlinx.coroutines.launch

data class TodoItem(val id: Long, val title: String, val done: Boolean = false)

// --------- simple screen enum ----------
enum class AppScreen {
    Home,
    Edit,
    Completed,
    About
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

// --------- Root composable with hamburger menu + navigation ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatTodoApp() {
    // Shared list so ALL screens see the same tasks
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
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Edit List") },
                    selected = currentScreen == AppScreen.Edit,
                    onClick = {
                        currentScreen = AppScreen.Edit
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Completed Tasks") },
                    selected = currentScreen == AppScreen.Completed,
                    onClick = {
                        currentScreen = AppScreen.Completed
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("About App") },
                    selected = currentScreen == AppScreen.About,
                    onClick = {
                        currentScreen = AppScreen.About
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
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
                            when (currentScreen) {
                                AppScreen.Home -> "Cat To-Do"
                                AppScreen.Edit -> "Edit To-Do List"
                                AppScreen.Completed -> "Completed Tasks"
                                AppScreen.About -> "About This App"
                            }
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

                AppScreen.Completed -> CompletedTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                AppScreen.About -> AboutScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

// --------- Home screen ----------
@Composable
fun CatTodoScreen(
    items: MutableList<TodoItem>,
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    // Stats for summary card
    val totalCount = items.size
    val doneCount = items.count { it.done }
    val remainingCount = totalCount - doneCount

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.cat_background),
            contentDescription = "Cat background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)
                )
        )

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

            Spacer(Modifier.height(8.dp))

            Surface(
                tonalElevation = 3.dp,
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total: $totalCount")
                    Text("Done: $doneCount")
                    Text("Left: $remainingCount")
                }
            }

            Spacer(Modifier.height(16.dp))

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

            if (items.isEmpty()) {
                Text(
                    text = "Nothing yet. Add your first task!",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

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
                            if (idx != -1) items[idx] = items[idx].copy(done = checked)
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

// --------- Edit screen ----------
@Composable
fun EditTodoScreen(
    items: MutableList<TodoItem>,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<TodoItem?>(null) }
    var editText by remember { mutableStateOf("") }
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
                    val isSelected = selectedItem?.id == item.id

                    Surface(
                        tonalElevation = if (isSelected) 4.dp else 1.dp,
                        shape = MaterialTheme.shapes.small,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.surface,
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

        if (selectedItem != null) {
            Text("Editing:", style = MaterialTheme.typography.labelLarge)
            Text(selectedItem!!.title, color = Color.Gray)

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
                Button(onClick = {
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
                }) {
                    Text("Save")
                }

                OutlinedButton(onClick = {
                    selectedItem = null
                    editText = ""
                }) {
                    Text("Cancel")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (items.isNotEmpty()) {
            OutlinedButton(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Clear entire list")
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all tasks?") },
            text = { Text("This will remove every item from your list.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        items.clear()
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

// --------- Completed Tasks Screen ----------
@Composable
fun CompletedTodoScreen(
    items: List<TodoItem>,
    modifier: Modifier = Modifier
) {
    val completed = items.filter { it.done }

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
            Text(
                text = "You haven't completed any tasks yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(completed, key = { it.id }) { item ->
                    Surface(
                        tonalElevation = 2.dp,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
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
}

// --------- About Screen ----------
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "About",
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Cat To-Do App",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "A simple task manager built with Kotlin and Jetpack Compose.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Features:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text("• Add, complete, and delete tasks")
        Text("• Separate Edit screen for updating tasks")
        Text("• Completed Tasks screen with filtered view")
        Text("• Navigation drawer with multiple screens")
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Built by Jay Young for a class project.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

// --------- Row composable ----------
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
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (item.done) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatTodoPreview() {
    MyApplicationForClassTheme {
        CatTodoScreen(
            items = remember { mutableStateListOf() },
            modifier = Modifier.fillMaxSize()
        )
    }
}
