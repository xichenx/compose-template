package com.xichen.dev.domain.model

data class PageResult<T>(
    val items: List<T>,
    val currentPage: Int,
    val hasMore: Boolean
)
