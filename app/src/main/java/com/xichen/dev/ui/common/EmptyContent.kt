package com.xichen.dev.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xichen.dev.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    title: UiText = UiText.StringResource(R.string.common_empty_title),
    description: UiText? = null,
    fullscreen: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    FeedbackContentLayout(
        title = title,
        message = description,
        modifier = modifier,
        fullscreen = fullscreen,
        leadingContent = icon,
        actionContent = action
    )
}
