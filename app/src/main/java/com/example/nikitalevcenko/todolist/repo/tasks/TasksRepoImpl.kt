package com.example.nikitalevcenko.todolist.repo.tasks

import android.content.Context
import com.example.nikitalevcenko.todolist.db.DB
import com.example.nikitalevcenko.todolist.repo.core.BaseRepoImpl
import io.reactivex.Completable

class TasksRepoImpl(context: Context, private val db: DB) : BaseRepoImpl(context), TasksRepo {

    override val tasks = db.tasksDao.readAll()

    override fun updateOrUpdateTask(task: Task) = Completable.fromAction {
        db.tasksDao.updateOrUpdate(task)
        updateWidget()
    }!!

    override fun getTask(taskId: Long) = db.tasksDao.read(taskId)
}