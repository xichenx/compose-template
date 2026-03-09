package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xichen.dev.ui.theme.appSpacing

data class AppFilterChipOption(
    val id: String,
    val label: UiText,
    val enabled: Boolean = true
)

@Composable
fun AppFilterChipRow(
    options: List<AppFilterChipOption>,
    selectedOptionIds: Set<String>,
    onOptionClick: (AppFilterChipOption) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = MaterialTheme.appSpacing.lg),
    itemSpacing: androidx.compose.ui.unit.Dp = MaterialTheme.appSpacing.sm
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(
            items = options,
            key = { option -> option.id }
        ) { option ->
            FilterChip(
                selected = option.id in selectedOptionIds,
                onClick = { onOptionClick(option) },
                enabled = option.enabled,
                label = {
                    Text(text = option.label.asString())
                }
            )
        }
    }
}
