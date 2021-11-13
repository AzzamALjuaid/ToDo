package com.example.todo.TaskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todo.database.Task
import com.example.todo.TaskRepository

class TaskListViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    fun saveTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun dateSortTask(): LiveData<List<Task>> {
        return taskRepository.dateSortTask()
    }

    fun titleSortTask(): LiveData<List<Task>> {
        return taskRepository.titleSortTask()
    }

    fun completeSortTask(): LiveData<List<Task>> {
        return taskRepository.completeSortTask()
    }

    fun comleteFilter(): LiveData<List<Task>>{
        return taskRepository.completeFilter()
    }
    fun inComleteFilter():LiveData<List<Task>>{
        return taskRepository.inCompleteFilter()
    }
}