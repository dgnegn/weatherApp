package com.aplussoft.todoapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aplussoft.todoapp.data.datasource.local.dao.TodoDao
import com.aplussoft.todoapp.data.datasource.local.model.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}