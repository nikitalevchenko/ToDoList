package com.example.nikitalevcenko.todolist.db

import android.arch.persistence.room.TypeConverter
import com.example.nikitalevcenko.todolist.db.entity.Priority
import java.util.*

class Converters {

    @TypeConverter
    fun fromPriority(priority: Priority): Int = priority.ordinal

    @TypeConverter
    fun toPriority(priority: Int): Priority = Priority.values()[priority]

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(date: Long?): Date? = date?.let { Date(it) }
}