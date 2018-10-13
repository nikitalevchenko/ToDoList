package com.example.nikitalevcenko.todolist.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class TaskEntity(

        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val name: String,
        val description: String,
        val priority: Priority,
        val showDate: Date,
        val deadlineDate: Date?

//        @PrimaryKey(autoGenerate = true)
//val id: Long = 0,
//val name: String,
//val description: String,
//val priority: Priority,

)

//enum class Priority { HIGH, NORMAL, LOW }
enum class Priority { HIGH, NORMAL, LOW }