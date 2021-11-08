package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TaskViewModel () : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val taskidLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
            Transformations.switchMap(taskidLiveData) { taskid ->
                taskRepository.getTask(taskid)
            }

    fun loadTask(taskid:UUID) {
        taskidLiveData.value = taskid
    }
}