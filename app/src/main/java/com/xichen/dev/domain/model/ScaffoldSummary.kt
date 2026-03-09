package com.xichen.dev.domain.model

data class ScaffoldSummary(
    val highlights: List<ScaffoldHighlight>,
    val lastUpdatedAtMillis: Long?,
    val hasCompletedSetupGuide: Boolean
)
