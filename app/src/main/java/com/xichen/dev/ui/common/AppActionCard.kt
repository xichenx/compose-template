package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xichen.dev.ui.theme.appElevation

@Composable
fun AppActionCard(
    title: UiText,
    description: UiText,
    primaryActionText: UiText,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryActionText: UiText? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    AppCard(
        modifier = modifier,
        tonalElevation = MaterialTheme.appElevation.low
    ) {
        androidx.compose.material3.Text(
            text = title.asString(),
            style = MaterialTheme.typography.titleMedium
        )
        androidx.compose.material3.Text(
            text = description.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AppPrimaryButton(
            text = primaryActionText.asString(),
            onClick = onPrimaryAction,
            modifier = Modifier.fillMaxWidth()
        )
        if (secondaryActionText != null && onSecondaryAction != null) {
            AppSecondaryButton(
                text = secondaryActionText.asString(),
                onClick = onSecondaryAction,
                modifier = Modifier.fillMaxWidth(),
                fillMaxWidth = true
            )
        }
    }
}
