package com.example.myapplicationforclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationforclass.ui.theme.MyApplicationForClassTheme
import com.example.myapplicationforclass.vm.TodoViewModel

// Main entry point for your app.
// This Activity hosts all the Jetpack Compose UI.
class MainActivity : ComponentActivity() {

    // We will create / get the ViewModel inside onCreate using ViewModelProvider.
    // (No compose-specific viewModel() extension needed.)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allow drawing behind system bars (status bar / nav bar).
        enableEdgeToEdge()

        // Create or retrieve the TodoViewModel scoped to this Activity.
        // ViewModelProvider ensures it survives config changes like rotation.
        val vm = ViewModelProvider(this)[TodoViewModel::class.java]

        // Set the root of the Compose UI tree.
        setContent {
            // Apply your Material 3 theme.
            MyApplicationForClassTheme {
                // Pass the ViewModel into your root composable.
                CatTodoApp(vm)
            }
        }
    }
}
