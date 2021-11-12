package com.example.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "task-database"


@Database(entities = [Task::class] , version=1)
@TypeConverters(TaskTypeConverters::class)
abstract class Taskdatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
}