package com.example.nikitalevcenko.todolist.modules.task.interactor

import com.example.nikitalevcenko.todolist.modules.core.ui.observeOnMainThread
import com.example.nikitalevcenko.todolist.repo.tasks.Task
import com.example.nikitalevcenko.todolist.repo.tasks.TasksRepo

class TaskInteractorImpl(private val tasksRepo: TasksRepo) : TaskInteractor {

    override fun updateOrUpdateTask(task: Task) = tasksRepo.updateOrUpdateTask(task).observeOnMainThread()

    override fun getTask(taskId: Long) = tasksRepo.getTask(taskId).observeOnMainThread()
}