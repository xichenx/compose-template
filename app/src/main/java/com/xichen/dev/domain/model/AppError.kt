package com.xichen.dev.domain.model

sealed interface AppError {
    data object Network : AppError
    data object Timeout : AppError
    data object Unauthorized : AppError
    data object EmptyData : AppError
    data class Server(
        val code: Int,
        val message: String?
    ) : AppError
    data class Local(val message: String) : AppError
    data class Unknown(val message: String) : AppError
}
