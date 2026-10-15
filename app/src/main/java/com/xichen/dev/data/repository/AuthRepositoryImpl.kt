package com.xichen.dev.data.repository

import com.xichen.dev.core.coroutine.AppDispatchers
import com.xichen.dev.data.common.appResultOf
import com.xichen.dev.data.local.datastore.AuthSessionDataSource
import com.xichen.dev.data.remote.datasource.AuthRemoteDataSource
import com.xichen.dev.data.remote.mapper.toDomain
import com.xichen.dev.data.remote.model.LoginRequestDto
import com.xichen.dev.data.remote.model.RegisterRequestDto
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.LoginSession
import com.xichen.dev.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authSessionDataSource: AuthSessionDataSource,
    private val dispatchers: AppDispatchers
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<LoginSession> {
        return withContext(dispatchers.io) {
            appResultOf {
                val session = authRemoteDataSource.login(
                    LoginRequestDto(email = email, password = password)
                ).toDomain()
                authSessionDataSource.saveSession(session)
                session
            }
        }
    }

    override suspend fun register(
        displayName: String,
        email: String,
        password: String
    ): AppResult<LoginSession> {
        return withContext(dispatchers.io) {
            appResultOf {
                val session = authRemoteDataSource.register(
                    RegisterRequestDto(displayName = displayName, email = email, password = password)
                ).toDomain()
                authSessionDataSource.saveSession(session)
                session
            }
        }
    }

    override suspend fun getCachedSession(): AppResult<LoginSession?> {
        return withContext(dispatchers.io) {
            appResultOf {
                val cached = authSessionDataSource.getSession()
                if (!cached.isLoggedIn) {
                    null
                } else {
                    LoginSession(
                        userId = cached.userId.orEmpty(),
                        displayName = cached.displayName.orEmpty(),
                        email = cached.email.orEmpty(),
                        token = cached.token.orEmpty()
                    )
                }
            }
        }
    }

    override suspend fun clearSession(): AppResult<Unit> {
        return withContext(dispatchers.io) {
            appResultOf { authSessionDataSource.clearSession() }
        }
    }
}
