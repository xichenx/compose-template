package com.xichen.dev.ui.common

import com.xichen.dev.R
import com.xichen.dev.domain.model.AppError

fun AppError.asUiText(): UiText {
    return when (this) {
        AppError.EmptyData -> UiText.StringResource(R.string.error_empty_data)
        AppError.Network -> UiText.StringResource(R.string.error_network)
        AppError.Timeout -> UiText.StringResource(R.string.error_timeout)
        AppError.Unauthorized -> UiText.StringResource(R.string.error_unauthorized)
        is AppError.Server -> {
            message?.takeIf { it.isNotBlank() }?.let(UiText::DynamicString)
                ?: UiText.StringResource(R.string.error_server_generic)
        }
        is AppError.Local -> UiText.DynamicString(message)
        is AppError.Unknown -> UiText.DynamicString(message)
    }
}
