package com.xichen.dev.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppStateContent(
    isLoading: Boolean,
    errorMessage: UiText?,
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
    loadingMessage: UiText? = null,
    errorTitle: UiText? = null,
    emptyTitle: UiText? = null,
    emptyDescription: UiText? = null,
    onRetry: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> {
            LoadingContent(
                modifier = modifier,
                message = loadingMessage
            )
        }

        errorMessage != null -> {
            ErrorContent(
                message = errorMessage,
                onRetry = onRetry,
                modifier = modifier,
                title = errorTitle ?: UiText.StringResource(com.xichen.dev.R.string.common_error_title)
            )
        }

        isEmpty -> {
            EmptyContent(
                modifier = modifier,
                title = emptyTitle ?: UiText.StringResource(com.xichen.dev.R.string.common_empty_title),
                description = emptyDescription
            )
        }

        else -> content()
    }
}
