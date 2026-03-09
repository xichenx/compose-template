package com.xichen.dev.data.remote.mapper

import com.xichen.dev.data.remote.model.LoginResponseDto
import com.xichen.dev.domain.model.LoginSession

fun LoginResponseDto.toDomain(): LoginSession {
    return LoginSession(
        userId = userId,
        displayName = displayName,
        email = email,
        token = token
    )
}
