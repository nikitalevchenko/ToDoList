package com.example.nikitalevcenko.todolist.repo.tasks

import com.example.nikitalevcenko.todolist.db.entity.TaskEntity
import com.example.nikitalevcenko.todolist.repo.core.BaseRepo
import io.reactivex.Completable
import io.reactivex.Flowable

interface TasksRepo : BaseRepo {

    val tasks: Flowable<List<Task>>

    fun updateOrUpdateTask(task: Task): Completable

    fun getTask(taskId: Long): Flowable<Task>
}

typealias Task = TaskEntity