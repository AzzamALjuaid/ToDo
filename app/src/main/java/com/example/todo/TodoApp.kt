package com.example.todo

import android.app.Application

class TodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}