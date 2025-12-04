package com.example.myapplicationforclass

// ---------- Imports: Android + Jetpack Compose UI + Material3 ----------
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

// ---------- Data model: one to-do item ----------
// Represents a single task in the list.
//  - id: unique identifier (used to update/delete without confusion)
//  - title: text the user typed for the task
//  - done: whether the task is completed (defaults to false)
data class TodoItem(val id: Long, val title: String, val done: Boolean = false)

// ---------- Simple screen enum for navigation ----------
// Defines the different screens in the app that we can show in the UI.
enum class AppScreen {
    Home,       // Main to-do list screen
    Edit,       // Screen where user edits existing tasks / clears list
    Completed,  // Screen that only shows completed tasks
    About       // Static screen describing the app
}

// ---------- Activity entry point ----------
// Standard Android entry point. Sets up Compose content.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Draws UI edge-to-edge (behind system bars)
        setContent {
            // Apply custom Material theme
            MyApplicationForClassTheme {
                // Call the root composable for the app UI
                CatTodoApp()
            }
        }
    }
}

// ---------- Root composable with hamburger menu + navigation ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatTodoApp() {
    // Shared list so ALL screens see and modify the same tasks.
    // remember + mutableStateListOf = observable list that triggers recomposition.
    val items = remember { mutableStateListOf<TodoItem>() }

    // Tracks which screen is currently active (Home by default).
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    // Drawer state for opening/closing the navigation drawer.
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // Coroutine scope to launch drawer open/close animations.
    val scope = rememberCoroutineScope()

    // Top-level navigation drawer for the whole app.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Drawer sheet: vertical panel that contains menu items.
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                // Menu option: go to main to-do list (Home screen)
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

                // Menu option: go to Edit screen (edit items / clear list)
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

                // Menu option: go to Completed Tasks screen
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

                // Menu option: go to About screen
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
        // Scaffold provides a standard app structure: top bar + content area.
        Scaffold(
            topBar = {
                TopAppBar(
                    // Title changes based on the currently selected screen.
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
                        // Hamburger icon that opens/closes the navigation drawer.
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
            // Choose which screen composable to show based on currentScreen.
            when (currentScreen) {
                // Main screen: add tasks, see stats, toggle done, delete items.
                AppScreen.Home -> CatTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                // Edit screen: tap task to edit text, or clear all tasks.
                AppScreen.Edit -> EditTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                // Completed screen: shows only done == true items.
                AppScreen.Completed -> CompletedTodoScreen(
                    items = items,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )

                // About screen: static info about the project.
                AppScreen.About -> AboutScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

// ---------- Home screen: add tasks + show list + stats ----------
@Composable
fun CatTodoScreen(
    items: MutableList<TodoItem>,
    modifier: Modifier = Modifier
) {
    // Local state for the text field where the user types new tasks.
    var input by remember { mutableStateOf("") }
    // Used to hide keyboard and clear focus after adding a task.
    val focus = LocalFocusManager.current

    // Pre-computed stats that are displayed in the summary card.
    val totalCount = items.size              // Total number of tasks
    val doneCount = items.count { it.done }  // Number of completed tasks
    val remainingCount = totalCount - doneCount

    Box(modifier = modifier) {
        // Background cat image stretched to fill the whole screen.
        Image(
            painter = painterResource(id = R.drawable.cat_background),
            contentDescription = "Cat background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Semi-transparent overlay to darken the background for readability.
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)
                )
        )

        // Main vertical layout for header, stats, input row, and list.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text at the top of the screen.
            Text(
                text = "My To-Do List 🐾",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // Summary card showing total / done / left counts.
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

            // Row for the text input field and Add button.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text field where user types a new task.
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Add a task…") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                // Add button: creates a new TodoItem and inserts at top of list.
                Button(
                    onClick = {
                        val trimmed = input.trim()
                        if (trimmed.isNotEmpty()) {
                            items.add(
                                0,
                                TodoItem(
                                    id = System.currentTimeMillis(), // simple unique id
                                    title = trimmed
                                )
                            )
                            input = ""          // Clear input field
                            focus.clearFocus()  // Hide keyboard / remove focus
                        }
                    }
                ) { Text("Add") }
            }

            Spacer(Modifier.height(12.dp))

            // Helper text when there are no tasks yet.
            if (items.isEmpty()) {
                Text(
                    text = "Nothing yet. Add your first task!",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            // Scrollable list of all tasks.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Render each TodoItem using TodoRow, keyed by id for stability.
                items(items, key = { it.id }) { item ->
                    TodoRow(
                        item = item,
                        // When checkbox changes, update the 'done' field for that item.
                        onCheckedChange = { checked ->
                            val idx = items.indexOfFirst { it.id == item.id }
                            if (idx != -1) items[idx] = items[idx].copy(done = checked)
                        },
                        // When delete icon is tapped, remove that item from the list.
                        onDelete = {
                            items.removeAll { it.id == item.id }
                        }
                    )
                }
                // Extra spacer at the bottom of the list.
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

// ---------- Edit screen: edit existing tasks + clear list ----------
@Composable
fun EditTodoScreen(
    items: MutableList<TodoItem>,
    modifier: Modifier = Modifier
) {
    // Which item is currently selected for editing (or null if none).
    var selectedItem by remember { mutableStateOf<TodoItem?>(null) }
    // Text being edited for the selected item.
    var editText by remember { mutableStateOf("") }
    // Controls visibility of the "Clear entire list" confirmation dialog.
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Instructions at the top of the screen.
        Text(
            text = "Tap an item to edit it",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(16.dp))

        // If there are no items, show a simple message instead of the list.
        if (items.isEmpty()) {
            Text(
                text = "No items to edit yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            // Scrollable list of tasks for editing.
            LazyColumn(
                modifier = Modifier
                    .weight(1f)      // Take available vertical space
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    // Check if this item is currently selected to highlight it.
                    val isSelected = selectedItem?.id == item.id

                    // Each item is shown inside a Surface that changes style when selected.
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
                                // When tapped, mark this item as selected and prefill edit text.
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
                            // Show item title
                            Text(
                                text = item.title,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            // If the task is done, show a "Done" label.
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

        // Extra editing controls are only visible when an item is selected.
        if (selectedItem != null) {
            Text("Editing:", style = MaterialTheme.typography.labelLarge)
            Text(selectedItem!!.title, color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            // Text field to enter the new title for the selected task.
            OutlinedTextField(
                value = editText,
                onValueChange = { editText = it },
                label = { Text("New text") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Save button: apply the edited text to the selected task.
                Button(onClick = {
                    val trimmed = editText.trim()
                    if (trimmed.isNotEmpty()) {
                        val id = selectedItem!!.id
                        val idx = items.indexOfFirst { it.id == id }
                        if (idx != -1) {
                            val old = items[idx]
                            items[idx] = old.copy(title = trimmed)
                        }
                        // Clear selection and edit text after saving.
                        selectedItem = null
                        editText = ""
                    }
                }) {
                    Text("Save")
                }

                // Cancel button: stop editing and reset fields.
                OutlinedButton(onClick = {
                    selectedItem = null
                    editText = ""
                }) {
                    Text("Cancel")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Button to clear the entire list, only shown if there are tasks.
        if (items.isNotEmpty()) {
            OutlinedButton(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth(),
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
                        items.clear()          // Remove all tasks
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

// ---------- Completed Tasks Screen: filter done == true ----------
@Composable
fun CompletedTodoScreen(
    items: List<TodoItem>,
    modifier: Modifier = Modifier
) {
    // Filter the list down to only completed tasks.
    val completed = items.filter { it.done }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Screen title.
        Text(
            text = "Completed Tasks",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(16.dp))

        // If no tasks are done yet, show a friendly message.
        if (completed.isEmpty()) {
            Text(
                text = "You haven't completed any tasks yet.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            // Scrollable list of only the completed tasks.
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
                            // Static "Done" label to clearly mark completion.
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

// ---------- About Screen: static app description ----------
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
        // Info icon at the top.
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "About",
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        // App name.
        Text(
            text = "Cat To-Do App",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        // Short subtitle explaining the app.
        Text(
            text = "A simple task manager built with Kotlin and Jetpack Compose.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        // Feature list.
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
        // Credit line with your name.
        Text(
            text = "Built by Jay Young for a class project.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

// ---------- Row composable: one task row with checkbox + delete ----------
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
            // Checkbox to mark task as done / not done.
            Checkbox(
                checked = item.done,
                onCheckedChange = onCheckedChange
            )
            Spacer(Modifier.width(8.dp))
            // Task title, with strikethrough applied if done == true.
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
            // Trash icon button to delete this task.
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

// ---------- Preview: allows layout preview in Android Studio ----------
@Preview(showBackground = true)
@Composable
fun CatTodoPreview() {
    MyApplicationForClassTheme {
        // Preview the main Home screen with an empty list.
        CatTodoScreen(
            items = remember { mutableStateListOf() },
            modifier = Modifier.fillMaxSize()
        )
    }
}

