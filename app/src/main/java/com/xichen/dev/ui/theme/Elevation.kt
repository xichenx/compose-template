package com.xichen.dev.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val low: Dp = 1.dp,
    val medium: Dp = 3.dp,
    val high: Dp = 6.dp
)

val LocalAppElevation = staticCompositionLocalOf { AppElevation() }
