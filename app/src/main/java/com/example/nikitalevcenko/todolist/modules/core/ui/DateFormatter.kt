package com.example.nikitalevcenko.todolist.modules.core.ui

import java.text.DateFormat.LONG
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter() {

    private val simpleDateFormat = SimpleDateFormat.getDateInstance(LONG)

    fun format(date: Date) = simpleDateFormat.format(date)
}