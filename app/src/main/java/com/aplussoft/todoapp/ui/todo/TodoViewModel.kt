package com.aplussoft.todoapp.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplussoft.todoapp.data.datasource.local.model.Todo
import com.aplussoft.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false,
    val message: String? = "",
    val todo: Todo? = null,
    val id: Int = 0,
    val title: String = "",
    val isDone: Boolean = false,
)


@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    private val _todoState = MutableStateFlow(TodoState())
    val todoState = _todoState

    // to handle snackbar event
    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()


    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            _todoState.update { it.copy(isLoading = true) }
            try {
                repository.allTodos
                    .collect { todos ->
                        if (todos.isEmpty()) {
                            _todoState.update { it.copy(isEmpty = true, isLoading = false) }
                        } else {
                            _todoState.update { it.copy(todos = todos, isLoading = false) }
                        }
                    }
            } catch (e: Exception) {
                _snackbarEvent.send(e.message ?: "Unknown error")
            }
        }
    }

    fun onTitleChange(title: String) {
        viewModelScope.launch {
            _todoState.update {
                it.copy(title = title)
            }
        }
    }

    fun onTodoClicked(todo: Todo) {
        _todoState.update {
            it.copy(
                id = todo.id,
                title = todo.title,
                isDone = todo.isDone,
                todo = todo,
            )
        }
    }

    fun onInsertTodo(todo: Todo) {

        _todoState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.upsert(todo).runCatching {
                _todoState.update {
                    it.copy(
                        todos = it.todos + todo,
                        isLoading = false,
                        id = 0,
                        title = "",
                        todo = null,
                        isEmpty = false
                    )
                }


            }.onFailure { ex ->
                _snackbarEvent.send(ex.message ?: "Error: Todo not inserted")
            }
        }

    }

    fun onDone(todo: Todo) {
        val updatedTodo = todo.copy(isDone = !todo.isDone)
        viewModelScope.launch {
            repository.upsert(updatedTodo).runCatching {
                _todoState.update {
                    it.copy(
                        todos = it.todos.map { currentTodo ->
                            if (currentTodo.id == updatedTodo.id) updatedTodo else currentTodo
                        },
                        isLoading = false,
                        id = 0,
                        title = "",
                        todo = null,
                        isEmpty = false
                    )
                }
            }.onFailure { ex ->
                _snackbarEvent.send(ex.message ?: "Something went wrong!")
            }
        }
    }

    fun onDelete(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo).runCatching {
                _todoState.update {
                    it.copy(
                        todos = it.todos - todo,
                        isLoading = false,
                        id = 0,
                        title = "",
                        todo = null,
                        isEmpty = false
                    )
                }
            }.onFailure { ex ->
                _snackbarEvent.send(ex.message ?: "Something went wrong!")
            }
        }
    }
}