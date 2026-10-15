package com.xichen.dev.domain.usecase

import com.xichen.dev.domain.model.LoginSession
import com.xichen.dev.domain.repository.AuthRepository
import com.xichen.dev.domain.usecase.base.BaseSuspendNoParamsUseCase
import javax.inject.Inject

class GetCachedSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : BaseSuspendNoParamsUseCase<LoginSession?>() {
    override suspend fun execute(): com.xichen.dev.domain.model.AppResult<LoginSession?> {
        return com.xichen.dev.domain.model.AppResult.Success(authRepository.getCachedSession())
    }
}
