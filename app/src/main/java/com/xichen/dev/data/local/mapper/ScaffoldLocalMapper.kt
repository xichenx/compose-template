package com.xichen.dev.data.local.mapper

import com.xichen.dev.data.local.entity.ScaffoldHighlightEntity
import com.xichen.dev.domain.model.ScaffoldHighlight

fun ScaffoldHighlightEntity.toDomain(): ScaffoldHighlight {
    return ScaffoldHighlight(
        id = id,
        title = title,
        description = description,
        stage = stage
    )
}

fun ScaffoldHighlight.toEntity(updatedAtMillis: Long): ScaffoldHighlightEntity {
    return ScaffoldHighlightEntity(
        id = id,
        title = title,
        description = description,
        stage = stage,
        updatedAtMillis = updatedAtMillis
    )
}
