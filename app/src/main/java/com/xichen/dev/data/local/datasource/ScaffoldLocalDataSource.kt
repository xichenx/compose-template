package com.xichen.dev.data.local.datasource

import com.xichen.dev.data.local.database.dao.ScaffoldHighlightDao
import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity
import javax.inject.Inject

class ScaffoldLocalDataSource @Inject constructor(
    private val scaffoldHighlightDao: ScaffoldHighlightDao
) {
    suspend fun getHighlights(): List<ScaffoldHighlightEntity> {
        return scaffoldHighlightDao.getAll()
    }

    suspend fun replaceHighlights(items: List<ScaffoldHighlightEntity>) {
        scaffoldHighlightDao.replaceAll(items)
    }
}
