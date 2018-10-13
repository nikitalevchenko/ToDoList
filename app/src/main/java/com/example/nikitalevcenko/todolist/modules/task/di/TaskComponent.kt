package com.example.nikitalevcenko.todolist.modules.task.di

import com.example.nikitalevcenko.todolist.modules.task.presentation.TaskPresenter
import dagger.Subcomponent

@Subcomponent(modules = [(TaskModule::class)])
interface TaskComponent {
    fun inject(taskPresenter: TaskPresenter)
}