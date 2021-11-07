package com.example.todo

import java.util.*

data class Task (val id: UUID = UUID.randomUUID(),
                 var title:String = "",
                 var description:String = "",
                 var dueDate: Date = Date(),
                 var creationDate: Date = Date(),
                 var isComplete: Boolean = false)
