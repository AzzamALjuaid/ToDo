package com.example.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id:UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM task ORDER BY dueDate")
    fun dateSortTask():LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY title")
    fun titleSortTask():LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY isComplete = 0")
    fun completeSortTask():LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isComplete = 0")
    fun inCompleteFilter():LiveData<List<Task>>
    @Query("SELECT * FROM task WHERE isComplete = 1")
    fun completeFilter():LiveData<List<Task>>
}