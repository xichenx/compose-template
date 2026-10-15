package com.xichen.dev.data.common

import com.xichen.dev.domain.model.AppResult
import kotlinx.coroutines.CancellationException

internal suspend fun <T> appResultOf(block: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        AppResult.Error(exception.toAppError())
    }
}
