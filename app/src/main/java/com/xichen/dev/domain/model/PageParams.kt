package com.xichen.dev.domain.model

data class PageParams(
    val page: Int = DEFAULT_PAGE,
    val pageSize: Int = DEFAULT_PAGE_SIZE
) {
    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 20
    }
}
