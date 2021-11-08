package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task (@PrimaryKey var id: UUID = UUID.randomUUID(),
                 var title:String = "",
                 var description:String = "",
                 var dueDate: Date = Date(),
                 var creationDate: Date = Date(),
                 var isComplete: Boolean = false)
