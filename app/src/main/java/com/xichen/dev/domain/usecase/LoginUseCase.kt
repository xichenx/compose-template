package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.LoginSession
import com.xichen.dev.domain.repository.AuthRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendUseCase
import javax.inject.Inject

data class LoginParams(
    val email: String,
    val password: String
)

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseSuspendUseCase<LoginParams, LoginSession>() {
    override suspend fun execute(params: LoginParams): AppResult<LoginSession> {
        return authRepository.login(
            email = params.email,
            password = params.password
        )
    }
}
