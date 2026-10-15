package com.xichen.dev.testutil

import com.xichen.dev.domain.model.AppError
import com.xichen.dev.domain.model.AppLanguage
import com.xichen.dev.domain.model.AppResult
import com.xichen.dev.domain.model.AppThemeMode
import com.xichen.dev.domain.model.LoginSession
import com.xichen.dev.domain.model.ScaffoldHighlight
import com.xichen.dev.domain.model.ScaffoldSummary
import com.xichen.dev.domain.repository.AuthRepository
import com.xichen.dev.domain.repository.LanguageRepository
import com.xichen.dev.domain.repository.ScaffoldRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

val testSession = LoginSession("user", "Demo", "demo@compose.dev", "demo-token")
val testHighlights = listOf(
    ScaffoldHighlight("mvi", "MVI", "State management", "UI"),
    ScaffoldHighlight("room", "Room", "Local cache", "Data")
)

class FakeAuthRepository : AuthRepository {
    var loginResult: AppResult<LoginSession> = AppResult.Success(testSession)
    var registerResult: AppResult<LoginSession> = AppResult.Success(testSession)
    var sessionResult: AppResult<LoginSession?> = AppResult.Success(null)
    var loginCalls = 0
    var registerCalls = 0
    var sessionCalls = 0
    var beforeRequest: suspend () -> Unit = {}

    override suspend fun login(email: String, password: String): AppResult<LoginSession> {
        loginCalls++
        beforeRequest()
        return loginResult
    }

    override suspend fun register(displayName: String, email: String, password: String): AppResult<LoginSession> {
        registerCalls++
        beforeRequest()
        return registerResult
    }

    override suspend fun getCachedSession(): AppResult<LoginSession?> {
        sessionCalls++
        beforeRequest()
        return sessionResult
    }

    override suspend fun clearSession(): AppResult<Unit> {
        sessionResult = AppResult.Success(null)
        return AppResult.Success(Unit)
    }
}

class FakeLanguageRepository : LanguageRepository {
    var languageResult: AppResult<AppLanguage> = AppResult.Success(AppLanguage.default)
    var updateResult: AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getAppLanguage(): AppResult<AppLanguage> = languageResult

    override suspend fun updateAppLanguage(language: AppLanguage): AppResult<Unit> {
        if (updateResult is AppResult.Success) languageResult = AppResult.Success(language)
        return updateResult
    }

    override suspend fun initializeAppLanguage(): AppResult<Unit> = updateResult
}

class FakeScaffoldRepository : ScaffoldRepository {
    var summaryResult: AppResult<ScaffoldSummary> = AppResult.Success(ScaffoldSummary(testHighlights, null, true))
    var updateResult: AppResult<Unit> = AppResult.Success(Unit)
    val themeMode = MutableStateFlow(AppThemeMode.default)
    val refreshRequests = mutableListOf<Boolean>()
    var beforeRequest: suspend () -> Unit = {}

    override suspend fun getScaffoldSummary(forceRefresh: Boolean): AppResult<ScaffoldSummary> {
        refreshRequests += forceRefresh
        beforeRequest()
        return summaryResult
    }

    override suspend fun markSetupGuideAsSeen(): AppResult<Unit> = AppResult.Success(Unit)
    override fun observeAppThemeMode(): Flow<AppThemeMode> = themeMode

    override suspend fun updateAppThemeMode(themeMode: AppThemeMode): AppResult<Unit> {
        if (updateResult is AppResult.Success) this.themeMode.value = themeMode
        return updateResult
    }
}
