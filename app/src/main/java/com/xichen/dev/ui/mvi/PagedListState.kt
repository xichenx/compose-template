package com.xichen.dev.ui.mvi

import com.xichen.dev.domain.model.PageParams

data class PagedListState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = PageParams.DEFAULT_PAGE,
    val pageSize: Int = PageParams.DEFAULT_PAGE_SIZE,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true
) {
    fun markRefreshing(): PagedListState<T> {
        return copy(isRefreshing = true, isLoadingMore = false)
    }

    fun markAppending(): PagedListState<T> {
        return copy(isLoadingMore = true)
    }

    fun replaceFirstPage(newItems: List<T>, hasMore: Boolean): PagedListState<T> {
        return copy(
            items = newItems,
            currentPage = PageParams.DEFAULT_PAGE,
            isRefreshing = false,
            isLoadingMore = false,
            hasMore = hasMore
        )
    }

    fun appendPage(newItems: List<T>, hasMore: Boolean): PagedListState<T> {
        return copy(
            items = items + newItems,
            currentPage = currentPage + 1,
            isRefreshing = false,
            isLoadingMore = false,
            hasMore = hasMore
        )
    }

    fun stopLoading(): PagedListState<T> {
        return copy(
            isRefreshing = false,
            isLoadingMore = false
        )
    }
}
