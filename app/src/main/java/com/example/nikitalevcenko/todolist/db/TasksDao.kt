package com.example.nikitalevcenko.todolist.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.nikitalevcenko.todolist.db.entity.TaskEntity
import io.reactivex.Flowable

@Dao
interface TasksDao {

    @Query("select * from TaskEntity where id = :taskId")
    fun read(taskId: Long): Flowable<TaskEntity>

    @Query("select * from TaskEntity order by priority, id")
    fun readAll(): Flowable<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrUpdate(taskEntity: TaskEntity)

    @Query("delete from TaskEntity where id = :id")
    fun delete(id: Long)

    @Query("delete from TaskEntity")
    fun deleteAll()
}