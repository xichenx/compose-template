package com.xichen.dev.ui.common

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String? = null,
    navigationText: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        expandedHeight = 56.dp,
        title = {
            Text(
                text = title.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            if (navigationText != null && onNavigationClick != null) {
                AppSecondaryButton(
                    text = navigationText,
                    onClick = onNavigationClick
                )
            }
        },
        actions = {
            actions()
        }
    )
}
