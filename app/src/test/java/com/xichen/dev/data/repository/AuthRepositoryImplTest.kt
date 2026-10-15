package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.local.datastore.AuthSessionDataSource
import com.xichen.dev.data.local.datastore.AuthSessionPreferences
import com.xichen.dev.data.remote.datasource.AuthRemoteDataSource
import com.xichen.dev.data.remote.model.LoginResponseDto
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

class AuthRepositoryImplTest {
    private val remote = mockk<AuthRemoteDataSource>()
    private val session = mockk<AuthSessionDataSource>()

    @Test
    fun missingTokenIsAnAbsentSession() = runTest {
        coEvery { session.getSession() } returns AuthSessionPreferences()
        val repository = AuthRepositoryImpl(remote, session, AppDispatchers(io = StandardTestDispatcher(testScheduler)))
        assertEquals(AppResult.Success(null), repository.getCachedSession())
    }

    @Test
    fun storedSessionIsRestored() = runTest {
        coEvery { session.getSession() } returns AuthSessionPreferences("id", "Demo", "demo@compose.dev", "token")
        val repository = AuthRepositoryImpl(remote, session, AppDispatchers(io = StandardTestDispatcher(testScheduler)))
        val result = repository.getCachedSession() as AppResult.Success
        assertEquals("token", result.data?.token)
    }

    @Test
    fun sessionReadFailureIsNotAnAbsentSession() = runTest {
        coEvery { session.getSession() } throws IOException("read failed")
        val repository = AuthRepositoryImpl(remote, session, AppDispatchers(io = StandardTestDispatcher(testScheduler)))
        assertTrue(repository.getCachedSession() is AppResult.Error)
    }

    @Test
    fun loginSavesSessionOnlyAfterRemoteSuccess() = runTest {
        coEvery { remote.login(any()) } returns LoginResponseDto("id", "Demo", "demo@compose.dev", "token")
        coEvery { session.saveSession(any()) } returns Unit
        val repository = AuthRepositoryImpl(remote, session, AppDispatchers(io = StandardTestDispatcher(testScheduler)))
        assertTrue(repository.login("demo@compose.dev", "password123") is AppResult.Success)
        coVerify(exactly = 1) { session.saveSession(match { it.token == "token" }) }
    }

    @Test
    fun loginCancellationDoesNotSaveSession() = runTest {
        coEvery { remote.login(any()) } throws CancellationException("cancelled")
        val repository = AuthRepositoryImpl(remote, session, AppDispatchers(io = StandardTestDispatcher(testScheduler)))
        try {
            repository.login("demo@compose.dev", "password123")
            fail("Expected cancellation")
        } catch (exception: CancellationException) {
            assertEquals("cancelled", exception.message)
        }
        coVerify(exactly = 0) { session.saveSession(any()) }
    }
}
