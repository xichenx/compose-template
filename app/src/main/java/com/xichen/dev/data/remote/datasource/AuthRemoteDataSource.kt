package com.xichen.dev.data.remote.datasource

import com.xichen.dev.data.remote.api.AuthApiService
import com.xichen.dev.data.remote.model.LoginRequestDto
import com.xichen.dev.data.remote.model.LoginResponseDto
import com.xichen.dev.data.remote.model.RegisterRequestDto
import javax.inject.Inject
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        delay(500)
        // TODO: backend ready后替换为真实 Retrofit 调用。
        authApiService.hashCode()
        return when {
            request.email != DEMO_EMAIL -> {
                throw HttpException(
                    Response.error<Any>(
                        401,
                        "Unauthorized".toResponseBody("text/plain".toMediaType())
                    )
                )
            }
            request.password != DEMO_PASSWORD -> throw IllegalArgumentException("账号或密码错误")
            else -> LoginResponseDto(
                userId = "demo-user",
                displayName = "Compose Demo",
                email = request.email,
                token = "token_demo_compose_template"
            )
        }
    }

    suspend fun register(request: RegisterRequestDto): LoginResponseDto {
        delay(700)
        // TODO: backend ready后替换为真实 Retrofit 调用。
        authApiService.hashCode()
        return when {
            request.displayName.isBlank() -> throw IllegalArgumentException("昵称不能为空")
            request.email == DEMO_EMAIL -> throw IllegalArgumentException("该邮箱已被注册")
            request.password.length < 6 -> throw IllegalArgumentException("密码长度不能少于 6 位")
            else -> LoginResponseDto(
                userId = "user_${request.email.substringBefore('@')}",
                displayName = request.displayName,
                email = request.email,
                token = "token_register_${request.email.substringBefore('@')}"
            )
        }
    }

    private companion object {
        const val DEMO_EMAIL = "demo@compose.dev"
        const val DEMO_PASSWORD = "password123"
    }
}
