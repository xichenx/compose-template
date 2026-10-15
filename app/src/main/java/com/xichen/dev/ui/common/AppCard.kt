package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.xichen.dev.ui.theme.appElevation
import com.xichen.dev.ui.theme.appRadius
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    shape: Shape = RoundedCornerShape(MaterialTheme.appRadius.large),
    tonalElevation: Dp = MaterialTheme.appElevation.medium,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.appSpacing.lg),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = tonalElevation)
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                verticalArrangement = verticalArrangement,
                content = content
            )
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = tonalElevation)
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                verticalArrangement = verticalArrangement,
                content = content
            )
        }
    }
}

@Composable
fun AppInfoCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleColor: Color = contentColor
) {
    AppCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = MaterialTheme.appElevation.low,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = titleColor
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor
        )
    }
}
