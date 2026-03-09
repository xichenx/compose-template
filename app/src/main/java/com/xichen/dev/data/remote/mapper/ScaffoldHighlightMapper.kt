package com.xichen.dev.data.remote.mapper

import com.xichen.dev.data.remote.model.ScaffoldHighlightDto
import com.xichen.dev.domain.model.ScaffoldHighlight

fun ScaffoldHighlightDto.toDomain(): ScaffoldHighlight {
    return ScaffoldHighlight(
        id = id,
        title = title,
        description = description,
        stage = stage
    )
}
