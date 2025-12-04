package com.example.myapplicationforclass

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.vm.TodoViewModel
import kotlinx.coroutines.launch

// Enum listing all the screens for the app.
enum class AppScreen { Home, Edit, Completed, About }

// Root composable that sets up the navigation drawer and top app bar,
// and decides which screen to show based on currentScreen.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatTodoApp(viewModel: TodoViewModel) {

    // Track which screen is currently shown.
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    // Drawer state for opening/closing the navigation drawer.
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Needed to launch suspend functions (open/close drawer).
    val scope = rememberCoroutineScope()

    // Top-level navigation drawer wrapping the whole app.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Side panel for drawer content (menu list).
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    modifier = Modifier.padding(16.dp)
                )

                // Menu option: Home / To-Do list.
                NavigationDrawerItem(
                    label = { Text("To-Do List") },
                    selected = currentScreen == AppScreen.Home,
                    onClick = {
                        currentScreen = AppScreen.Home
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.List, contentDescription = null) }
                )

                // Menu option: Edit screen.
                NavigationDrawerItem(
                    label = { Text("Edit List") },
                    selected = currentScreen == AppScreen.Edit,
                    onClick = {
                        currentScreen = AppScreen.Edit
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) }
                )

                // Menu option: Completed tasks screen.
                NavigationDrawerItem(
                    label = { Text("Completed Tasks") },
                    selected = currentScreen == AppScreen.Completed,
                    onClick = {
                        currentScreen = AppScreen.Completed
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Check, contentDescription = null) }
                )

                // Menu option: About screen.
                NavigationDrawerItem(
                    label = { Text("About App") },
                    selected = currentScreen == AppScreen.About,
                    onClick = {
                        currentScreen = AppScreen.About
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) }
                )
            }
        }
    ) {
        // Scaffold gives us a standard layout: top bar + content area.
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // Change the title depending on which screen we're on.
                        Text(
                            when (currentScreen) {
                                AppScreen.Home -> "Cat To-Do"
                                AppScreen.Edit -> "Edit Tasks"
                                AppScreen.Completed -> "Completed Tasks"
                                AppScreen.About -> "About This App"
                            }
                        )
                    },
                    navigationIcon = {
                        // Hamburger icon for opening/closing the drawer.
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Content of the screen is chosen based on currentScreen.
            when (currentScreen) {
                AppScreen.Home ->
                    CatTodoScreen(viewModel, Modifier.padding(innerPadding))

                AppScreen.Edit ->
                    EditTodoScreen(viewModel, Modifier.padding(innerPadding))

                AppScreen.Completed ->
                    CompletedTodoScreen(viewModel, Modifier.padding(innerPadding))

                AppScreen.About ->
                    AboutScreen(Modifier.padding(innerPadding))
            }
        }
    }
}
