package com.example.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todo.Task

@Database(entities = [Task::class] , version=1)
@TypeConverters(TaskTypeConverters::class)
abstract class Taskdatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
}