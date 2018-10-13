package com.example.nikitalevcenko.todolist.modules.task.di

import android.support.annotation.NonNull
import com.example.nikitalevcenko.todolist.modules.task.interactor.TaskInteractor
import com.example.nikitalevcenko.todolist.modules.task.interactor.TaskInteractorImpl
import com.example.nikitalevcenko.todolist.repo.tasks.TasksRepo
import dagger.Module
import dagger.Provides

@Module
class TaskModule {
    @Provides
    @NonNull
    fun interactor(tasksRepo: TasksRepo): TaskInteractor = TaskInteractorImpl(tasksRepo)
}