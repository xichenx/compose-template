package com.xichen.dev.data.common

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppResult
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class AppResultOfTest {
    @Test
    fun successfulAndNullableValuesArePreserved() = runTest {
        assertEquals(AppResult.Success(42), appResultOf { 42 })
        assertEquals(AppResult.Success<String?>(null), appResultOf<String?> { null })
    }

    @Test
    fun expectedExceptionsBecomeAppErrors() = runTest {
        assertEquals(AppResult.Error(AppError.Network), appResultOf<Unit> { throw IOException("offline") })
        assertEquals(AppResult.Error(AppError.Timeout), appResultOf<Unit> { throw SocketTimeoutException() })
    }

    @Test
    fun cancellationIsRethrownUnchanged() = runTest {
        val cancellation = CancellationException("cancelled")
        try {
            appResultOf<Unit> { throw cancellation }
            fail("Cancellation must not become an error result")
        } catch (exception: CancellationException) {
            assertSame(cancellation, exception)
        }
    }

    @Test
    fun fatalErrorsAreNotConverted() = runTest {
        val error = AssertionError("programming error")
        try {
            appResultOf<Unit> { throw error }
            fail("Fatal errors must propagate")
        } catch (actual: AssertionError) {
            assertSame(error, actual)
        }
    }
}
