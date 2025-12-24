package com.aplussoft.todoapp.data.repository

import com.aplussoft.todoapp.data.datasource.local.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    val allTodos: Flow<List<Todo>>
    suspend fun upsert(todo: Todo)
    suspend fun delete(todo: Todo)

}


