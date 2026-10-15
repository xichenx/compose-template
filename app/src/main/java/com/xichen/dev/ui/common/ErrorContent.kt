package com.xichen.dev.ui.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xichen.dev.R

@Composable
fun ErrorContent(
    message: UiText,
    onRetry: (() -> Unit)?,
    modifier: Modifier = Modifier,
    title: UiText = UiText.StringResource(R.string.common_error_title),
    retryText: UiText = UiText.StringResource(R.string.common_retry),
    fullscreen: Boolean = true,
    action: (@Composable () -> Unit)? = null
) {
    val actionContent = action ?: onRetry?.let {
        @Composable {
            Button(onClick = it) {
                Text(text = retryText.asString())
            }
        }
    }

    FeedbackContentLayout(
        title = title,
        message = message,
        modifier = modifier,
        fullscreen = fullscreen,
        actionContent = actionContent
    )
}
