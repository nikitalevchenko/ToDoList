package com.example.nikitalevcenko.todolist.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.example.nikitalevcenko.todolist.db.entity.TaskEntity
import java.util.*


@Database(
        entities = [(TaskEntity::class)],
        version = 2,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase() {
    abstract val tasksDao: TasksDao

    companion object {
        fun buildDB(context: Context) = Room.databaseBuilder(context, DB::class.java, "database.db")
                .addMigrations(MIGRATION_1_2)
                .build()

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE TaskEntity ADD COLUMN showDate INTEGER NOT NULL DEFAULT ${Date().time}")
                database.execSQL("ALTER TABLE TaskEntity ADD COLUMN deadlineDate INTEGER")
            }
        }
    }
}