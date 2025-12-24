package com.aplussoft.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.aplussoft.todoapp.ui.theme.TodoAppTheme
import com.aplussoft.todoapp.ui.todo.TodoScreen
import com.aplussoft.todoapp.ui.todo.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // change the status bar's color to transparent and icons' to white
        enableEdgeToEdge(
            SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            TodoAppTheme {
                TodoScreen(viewModel = viewModel)
            }
        }
    }
}
