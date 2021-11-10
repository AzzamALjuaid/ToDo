package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    fun sortTask(): LiveData<List<Task>> {
       return taskRepository.sortTask()

    }
}