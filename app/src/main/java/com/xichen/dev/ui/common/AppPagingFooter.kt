package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xichen.dev.R
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AppPagingFooter(
    isLoadingMore: Boolean,
    hasMore: Boolean,
    modifier: Modifier = Modifier,
    appendError: UiText? = null,
    onRetry: (() -> Unit)? = null,
    loadingText: UiText = UiText.StringResource(R.string.common_loading_more),
    endReachedText: UiText = UiText.StringResource(R.string.common_no_more_data),
    retryText: UiText = UiText.StringResource(R.string.common_retry)
) {
    when {
        isLoadingMore -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = loadingText.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        appendError != null && onRetry != null -> {
            ErrorContent(
                message = appendError,
                onRetry = onRetry,
                modifier = modifier,
                fullscreen = false,
                retryText = retryText
            )
        }

        !hasMore -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = endReachedText.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
