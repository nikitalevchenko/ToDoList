package com.example.nikitalevcenko.todolist


import com.example.nikitalevcenko.todolist.modules.task.di.TaskComponent
import com.example.nikitalevcenko.todolist.modules.task.di.TaskModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(app: App)

    fun plus(taskModule: TaskModule): TaskComponent
}