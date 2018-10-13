package com.example.nikitalevcenko.todolist.repo.core

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.example.nikitalevcenko.todolist.modules.widget.TasksWidget

abstract class BaseRepoImpl(private val context: Context) : BaseRepo {

    override fun updateWidget() {
        context.sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
            component = ComponentName(context, TasksWidget::class.java)
        })
    }
}