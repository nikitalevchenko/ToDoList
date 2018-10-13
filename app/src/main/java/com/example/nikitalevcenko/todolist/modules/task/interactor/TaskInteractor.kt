package com.example.nikitalevcenko.todolist.modules.task.interactor

import com.example.nikitalevcenko.todolist.repo.tasks.Task
import io.reactivex.Completable
import io.reactivex.Flowable

interface TaskInteractor {
    fun updateOrUpdateTask(task: Task): Completable
    fun getTask(taskId: Long): Flowable<Task>
}