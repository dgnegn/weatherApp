package com.aplussoft.todoapp.ui.todo


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.aplussoft.todoapp.data.datasource.local.model.Todo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel = hiltViewModel<TodoViewModel>()) {

    val state by viewModel.todoState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo App") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                state.isLoading -> LoadingContent()
                state.isEmpty -> EmptyContent(
                    state = state,
                    onAdd = viewModel::onInsertTodo,
                    onTitleChange = viewModel::onTitleChange
                )

                state.todos.isNotEmpty() -> TodoList(
                    state = state,
                    onClicked = viewModel::onTodoClicked,
                    onDone = viewModel::onDone,
                    onInsert = viewModel::onInsertTodo,
                    onTitleChange = viewModel::onTitleChange,
                    onDelete = viewModel::onDelete
                )

            }
        }
    }
}

@Composable
fun TodoList(
    state: TodoState,
    onClicked: (Todo) -> Unit,
    onDone: (Todo) -> Unit,
    onInsert: (Todo) -> Unit,
    onTitleChange: (String) -> Unit,
    onDelete: (Todo) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        LazyColumn {
            items(state.todos) { todo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { onClicked(todo) }
                        )

                ) {
                    Text(
                        text = todo.title,
                        textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    if (todo.isDone) {
                        IconButton(
                            onClick = { onDelete(todo) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    Checkbox(
                        checked = todo.isDone,
                        onCheckedChange = { onDone(todo) }
                    )


                }
            }

        }
        AddTodoContent(
            state = state,
            onAdd = onInsert,
            onTitleChange = onTitleChange
        )
    }
}


@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyContent(
    state: TodoState,
    onAdd: (Todo) -> Unit,
    onTitleChange: (String) -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.81f)
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(text = "No todos found!", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        AddTodoContent(
            state = state,
            onAdd = onAdd,
            onTitleChange = onTitleChange
        )
    }
}

@Composable
fun AddTodoContent(
    state: TodoState,
    onAdd: (Todo) -> Unit,
    onTitleChange: (title: String) -> Unit,
) {
    TextField(
        value = state.title,
        onValueChange = { onTitleChange(it) },
        placeholder = { Text("Add a todo") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, bottom = 16.dp)
            .clip(MaterialTheme.shapes.extraLarge),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background
        ),
        trailingIcon = {
            IconButton(
                modifier = Modifier
                    .padding(end = 8.dp),
                enabled = state.title.isNotBlank(),
                onClick = { onAdd(Todo(id = state.id, title = state.title)) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = if (state.title.isNotBlank()) MaterialTheme.colorScheme.surface else Color.Gray,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.primary,
                        )


                )
            }
        }
    )
}