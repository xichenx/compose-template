package com.xichen.dev.data.local.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScaffoldHighlightDaoTest {
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun replacementRemovesOldRowsAndStoresNewRows() = runBlocking {
        val dao = database.scaffoldHighlightDao()
        dao.replaceAll(listOf(item("old")))
        dao.replaceAll(listOf(item("new")))
        assertEquals(listOf("new"), dao.getAll().map { it.id })
    }

    @Test
    fun failedInsertRollsBackDeletion() = runBlocking {
        val dao = database.scaffoldHighlightDao()
        dao.replaceAll(listOf(item("old")))
        database.openHelper.writableDatabase.execSQL(
            "CREATE TRIGGER reject_bad BEFORE INSERT ON scaffold_highlight " +
                "WHEN NEW.id = 'bad' BEGIN SELECT RAISE(ABORT, 'reject bad row'); END"
        )
        try {
            dao.replaceAll(listOf(item("new"), item("bad")))
            fail("Expected insert failure")
        } catch (exception: android.database.sqlite.SQLiteException) {
            assertEquals(listOf("old"), dao.getAll().map { it.id })
        }
    }

    private fun item(id: String) = ScaffoldHighlightEntity(id, id, "description", "stage", 1L)
}
