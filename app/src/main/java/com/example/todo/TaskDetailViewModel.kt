package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TaskDetailViewModel () : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
            Transformations.switchMap(taskIdLiveData) { taskId ->
                taskRepository.getTask(taskId)
            }

    fun loadTask(taskId:UUID) {
        taskIdLiveData.value = taskId
    }
    fun deletTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun saveTask(task: Task) {
        taskRepository.updateTask(task)
    }
}