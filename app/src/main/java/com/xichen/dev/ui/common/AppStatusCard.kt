package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.xichen.dev.ui.theme.appElevation

@Composable
fun AppStatusCard(
    title: UiText,
    description: UiText,
    modifier: Modifier = Modifier,
    label: UiText = UiText.Empty,
    supportingText: UiText? = null,
    actionText: UiText? = null,
    onActionClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    AppCard(
        modifier = modifier,
        containerColor = containerColor,
        tonalElevation = MaterialTheme.appElevation.medium
    ) {
        if (label != UiText.Empty) {
            Text(
                text = label.asString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = title.asString(),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description.asString(),
            style = MaterialTheme.typography.bodyMedium
        )
        if (supportingText != null && supportingText != UiText.Empty) {
            Text(
                text = supportingText.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (actionText != null && onActionClick != null) {
            AppPrimaryButton(
                text = actionText.asString(),
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
