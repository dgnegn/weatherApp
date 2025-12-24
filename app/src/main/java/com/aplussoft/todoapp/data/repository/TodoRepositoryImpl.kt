package com.aplussoft.todoapp.data.repository

import com.aplussoft.todoapp.data.datasource.local.dao.TodoDao
import com.aplussoft.todoapp.data.datasource.local.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao  // to local datasource
) : TodoRepository {
    override val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()
    override suspend fun upsert(todo: Todo) = todoDao.upsert(todo)
    override suspend fun delete(todo: Todo) = todoDao.delete(todo)
}

