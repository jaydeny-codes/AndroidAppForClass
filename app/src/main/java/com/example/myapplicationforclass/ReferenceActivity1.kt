package com.example.myapplicationforclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplicationforclass.ui.theme.MyApplicationForClassTheme

// ---------- 4 reference activities (one for each screen) ----------

class ReferenceActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationForClassTheme {
                ReferenceScreen(
                    title = "Reference Activity 1 – Home / To-Do Screen",
                    body = """
                        This screen shows the main to-do list.
                        • Displays total, done, and remaining task counts.
                        • Lets the user add new tasks with an input box and Add button.
                        • Shows all tasks with a checkbox and delete icon.
                        • Background is a cat image with a dark overlay.
                    """.trimIndent()
                )
            }
        }
    }
}

class ReferenceActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationForClassTheme {
                ReferenceScreen(
                    title = "Reference Activity 2 – Edit Screen",
                    body = """
                        This screen lets the user edit existing tasks.
                        • Shows the full list of tasks in a scrollable column.
                        • Tapping an item selects it and fills the edit text field.
                        • User can Save changes or Cancel editing.
                        • Includes a button to clear the entire list with a confirm dialog.
                    """.trimIndent()
                )
            }
        }
    }
}

class ReferenceActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationForClassTheme {
                ReferenceScreen(
                    title = "Reference Activity 3 – Completed Tasks Screen",
                    body = """
                        This screen filters and shows only completed tasks.
                        • Displays a title 'Completed Tasks'.
                        • If there are no completed tasks, it shows a friendly message.
                        • Otherwise, it lists each completed task with a 'Done' label.
                    """.trimIndent()
                )
            }
        }
    }
}

class ReferenceActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationForClassTheme {
                ReferenceScreen(
                    title = "Reference Activity 4 – About Screen",
                    body = """
                        This screen describes the Cat To-Do app.
                        • Shows the app name and a short description.
                        • Lists key features (add, edit, and complete tasks).
                        • Includes a credit line with the author name and purpose.
                    """.trimIndent()
                )
            }
        }
    }
}

// Shared simple UI used by all reference activities
@Composable
fun ReferenceScreen(
    title: String,
    body: String
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}
