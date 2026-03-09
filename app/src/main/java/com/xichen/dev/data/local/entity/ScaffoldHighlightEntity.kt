package com.xichen.dev.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scaffold_highlight")
data class ScaffoldHighlightEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val stage: String,
    val updatedAtMillis: Long
)
