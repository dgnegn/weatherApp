package com.aplussoft.todoapp.di

import android.content.Context
import androidx.room.Room
import com.aplussoft.todoapp.data.datasource.local.TodoDatabase
import com.aplussoft.todoapp.data.datasource.local.dao.TodoDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase =
        Room.databaseBuilder(context, TodoDatabase::class.java, "todo_database").build()

    @Provides
    @Singleton
    fun provideTodoDao(db: TodoDatabase): TodoDao = db.todoDao()
}
