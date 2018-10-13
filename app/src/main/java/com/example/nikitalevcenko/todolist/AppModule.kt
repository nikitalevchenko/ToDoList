package com.example.nikitalevcenko.todolist

import android.arch.persistence.room.Room
import android.content.Context
import android.support.annotation.NonNull
import com.example.nikitalevcenko.todolist.db.DB
import com.example.nikitalevcenko.todolist.repo.tasks.TasksRepo
import com.example.nikitalevcenko.todolist.repo.tasks.TasksRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun context() = appContext

    @Provides
    @NonNull
    @Singleton
    fun db(context: Context): DB = DB.buildDB(context)

    @Provides
    @NonNull
    @Singleton
    fun tasksRepo(context: Context, db: DB): TasksRepo = TasksRepoImpl(context, db)
}