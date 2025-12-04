package com.example.myapplicationforclass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Simple static screen describing the app and its features.
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cat To-Do App",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "A simple to-do list built with Kotlin and Jetpack Compose."
        )

        Spacer(Modifier.height(16.dp))

        Text("Features:")
        Text("• Add, edit, complete, and delete tasks")
        Text("• Separate Edit and Completed screens")
        Text("• Navigation drawer between screens")
    }
}
