package com.aplussoft.todoapp.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.aplussoft.todoapp.data.datasource.local.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Upsert
    suspend fun upsert(todo: Todo)
    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todos ORDER BY isDone ASC")
    fun getAllTodos(): Flow<List<Todo>>
}
