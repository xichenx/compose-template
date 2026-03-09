package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AppListHeader(
    title: UiText,
    modifier: Modifier = Modifier,
    description: UiText? = null,
    supportingText: UiText? = null,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title.asString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            action?.invoke()
        }
        if (description != null && description != UiText.Empty) {
            Text(
                text = description.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (supportingText != null && supportingText != UiText.Empty) {
            Text(
                text = supportingText.asString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
