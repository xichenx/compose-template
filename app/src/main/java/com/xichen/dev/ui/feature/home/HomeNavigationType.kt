package com.xichen.dev.ui.feature.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class HomeNavigationType {
    BottomBar,
    NavigationRail,
    PermanentDrawer
}

fun WindowWidthSizeClass.toHomeNavigationType(): HomeNavigationType {
    return when (this) {
        WindowWidthSizeClass.Compact -> HomeNavigationType.BottomBar
        WindowWidthSizeClass.Medium -> HomeNavigationType.NavigationRail
        WindowWidthSizeClass.Expanded -> HomeNavigationType.PermanentDrawer
        else -> HomeNavigationType.BottomBar
    }
}
