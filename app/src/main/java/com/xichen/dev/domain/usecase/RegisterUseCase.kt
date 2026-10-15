package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.LoginSession
import com.xichen.dev.domain.repository.AuthRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

data class RegisterParams(
    val displayName: String,
    val email: String,
    val password: String
)

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseSuspendUseCase<RegisterParams, LoginSession>() {
    override suspend fun execute(params: RegisterParams): AppResult<LoginSession> {
        return authRepository.register(
            displayName = params.displayName,
            email = params.email,
            password = params.password
        )
    }
}
