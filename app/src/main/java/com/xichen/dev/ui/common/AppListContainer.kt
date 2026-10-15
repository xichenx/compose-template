package com.xichen.dev.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AppListContainer(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = MaterialTheme.appSpacing.lg,
        vertical = MaterialTheme.appSpacing.lg
    ),
    verticalItemSpacing: Dp = MaterialTheme.appSpacing.lg,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(verticalItemSpacing),
        content = content
    )
}
