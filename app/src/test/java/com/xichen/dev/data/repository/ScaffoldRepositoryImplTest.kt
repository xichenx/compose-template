package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.local.datasource.ScaffoldLocalDataSource
import com.xichen.dev.data.local.datastore.ScaffoldPreferences
import com.xichen.dev.data.local.datastore.ScaffoldPreferencesDataSource
import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity
import com.xichen.dev.data.remote.datasource.ScaffoldRemoteDataSource
import com.xichen.dev.data.remote.model.ScaffoldHighlightDto
import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ScaffoldRepositoryImplTest {
    private val remote = mockk<ScaffoldRemoteDataSource>()
    private val local = mockk<ScaffoldLocalDataSource>()
    private val preferences = mockk<ScaffoldPreferencesDataSource>()
    private val cached = ScaffoldHighlightEntity("cached", "Cache", "Local", "Data", 1L)
    private val fresh = ScaffoldHighlightDto("fresh", "Fresh", "Remote", "Data")

    @Test
    fun cacheHitSkipsRemoteRequest() = runTest {
        coEvery { local.getHighlights() } returns listOf(cached)
        coEvery { preferences.getPreferences() } returns ScaffoldPreferences()
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = ScaffoldRepositoryImpl(remote, local, preferences, AppDispatchers(io = dispatcher))
        val result = repository.getScaffoldSummary(false)
        assertTrue(result is AppResult.Success)
        assertEquals("cached", (result as AppResult.Success).data.highlights.single().id)
        coVerify(exactly = 0) { remote.fetchHighlights() }
    }

    @Test
    fun forcedRefreshReplacesCache() = runTest {
        coEvery { local.getHighlights() } returns listOf(cached)
        coEvery { remote.fetchHighlights() } returns listOf(fresh)
        coEvery { local.replaceHighlights(any()) } returns Unit
        coEvery { preferences.updateLastUpdatedAt(any()) } returns Unit
        coEvery { preferences.markSetupGuideAsSeen() } returns Unit
        coEvery { preferences.getPreferences() } returns ScaffoldPreferences()
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = ScaffoldRepositoryImpl(remote, local, preferences, AppDispatchers(io = dispatcher))
        val result = repository.getScaffoldSummary(true)
        assertEquals("fresh", (result as AppResult.Success).data.highlights.single().id)
        coVerify(exactly = 1) { local.replaceHighlights(match { it.single().id == "fresh" }) }
    }

    @Test
    fun remoteFailureDoesNotReplaceCache() = runTest {
        coEvery { local.getHighlights() } returns listOf(cached)
        coEvery { remote.fetchHighlights() } throws IOException("offline")
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = ScaffoldRepositoryImpl(remote, local, preferences, AppDispatchers(io = dispatcher))
        assertEquals(AppResult.Error(AppError.Network), repository.getScaffoldSummary(true))
        coVerify(exactly = 0) { local.replaceHighlights(any()) }
    }

    @Test
    fun emptyRemoteResultDoesNotEraseCache() = runTest {
        coEvery { local.getHighlights() } returns listOf(cached)
        coEvery { remote.fetchHighlights() } returns emptyList()
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = ScaffoldRepositoryImpl(remote, local, preferences, AppDispatchers(io = dispatcher))
        assertEquals(AppResult.Error(AppError.EmptyData), repository.getScaffoldSummary(true))
        coVerify(exactly = 0) { local.replaceHighlights(any()) }
    }

    @Test
    fun cancellationPropagatesWithoutWritingCache() = runTest {
        coEvery { local.getHighlights() } returns emptyList()
        coEvery { remote.fetchHighlights() } throws CancellationException("cancelled")
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repository = ScaffoldRepositoryImpl(remote, local, preferences, AppDispatchers(io = dispatcher))
        try {
            repository.getScaffoldSummary(true)
            fail("Expected cancellation")
        } catch (exception: CancellationException) {
            assertEquals("cancelled", exception.message)
        }
        coVerify(exactly = 0) { local.replaceHighlights(any()) }
    }
}
