package com.xichen.dev.data.common

import android.database.sqlite.SQLiteException
import com.xichen.dev.domain.model.AppError
import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

fun Throwable.toAppError(): AppError {
    return when (this) {
        is SocketTimeoutException -> AppError.Timeout
        is HttpException -> {
            if (code() == 401) {
                AppError.Unauthorized
            } else {
                AppError.Server(code = code(), message = message())
            }
        }
        is IOException -> AppError.Network
        is SQLiteException -> AppError.Local(message ?: "Local database operation failed")
        is IllegalArgumentException -> AppError.Local(message ?: "Invalid local input")
        else -> AppError.Unknown(message ?: "Unknown error")
    }
}
