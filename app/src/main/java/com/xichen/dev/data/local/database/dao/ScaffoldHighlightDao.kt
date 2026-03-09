package com.xichen.dev.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity

@Dao
interface ScaffoldHighlightDao {
    @Query("SELECT * FROM scaffold_highlight ORDER BY updatedAtMillis DESC")
    suspend fun getAll(): List<ScaffoldHighlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ScaffoldHighlightEntity>)

    @Query("DELETE FROM scaffold_highlight")
    suspend fun clearAll()
}
