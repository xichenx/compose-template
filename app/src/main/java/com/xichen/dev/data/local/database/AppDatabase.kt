package com.xichen.dev.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xichen.dev.data.local.database.dao.ScaffoldHighlightDao
import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity

@Database(
    entities = [ScaffoldHighlightEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scaffoldHighlightDao(): ScaffoldHighlightDao
}
