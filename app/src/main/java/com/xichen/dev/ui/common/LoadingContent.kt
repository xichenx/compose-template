package com.xichen.dev.ui.common

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xichen.dev.R

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    message: UiText? = UiText.StringResource(R.string.common_loading),
    fullscreen: Boolean = true,
    indicator: @Composable (() -> Unit)? = null
) {
    FeedbackContentLayout(
        message = message,
        modifier = modifier,
        fullscreen = fullscreen,
        leadingContent = {
            indicator?.invoke() ?: CircularProgressIndicator()
        }
    )
}
