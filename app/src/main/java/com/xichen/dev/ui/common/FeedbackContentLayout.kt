package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.xichen.dev.ui.theme.appSpacing

@Composable
internal fun FeedbackContentLayout(
    title: UiText = UiText.Empty,
    message: UiText? = null,
    modifier: Modifier = Modifier,
    fullscreen: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable () -> Unit)? = null
) {
    val layoutModifier = if (fullscreen) {
        modifier.fillMaxSize()
    } else {
        modifier.fillMaxWidth()
    }

    Column(
        modifier = layoutModifier.padding(MaterialTheme.appSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        leadingContent?.invoke()
        if (title != UiText.Empty) {
            Text(
                text = title.asString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
        if (message != null && message != UiText.Empty) {
            Text(
                text = message.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        actionContent?.invoke()
    }
}
