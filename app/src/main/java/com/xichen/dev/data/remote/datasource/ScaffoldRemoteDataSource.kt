package com.xichen.dev.data.remote.datasource

import com.xichen.dev.data.remote.api.ScaffoldApiService
import com.xichen.dev.data.remote.model.ScaffoldHighlightDto
import javax.inject.Inject
import kotlinx.coroutines.delay

class ScaffoldRemoteDataSource @Inject constructor(
    private val apiService: ScaffoldApiService
) {
    suspend fun fetchHighlights(): List<ScaffoldHighlightDto> {
        delay(300)
        // TODO: backend ready后改为真实 Retrofit 调用，目前先保留本地 mock 数据确保脚手架开箱可运行。
        return listOf(
            ScaffoldHighlightDto(
                id = "mvi",
                title = "MVI 状态驱动",
                description = "页面状态统一由 StateFlow 驱动，一次性事件走 Effect。",
                stage = "基础能力"
            ),
            ScaffoldHighlightDto(
                id = "navigation",
                title = "Navigation Compose",
                description = "默认预留导航骨架，后续可直接扩展为多页面应用。",
                stage = "路由层"
            ),
            ScaffoldHighlightDto(
                id = "room",
                title = "Room 本地缓存",
                description = "列表结果可缓存到本地数据库，便于离线读取和后续扩展。",
                stage = "数据层"
            ),
            ScaffoldHighlightDto(
                id = "clean-architecture",
                title = "Clean Architecture",
                description = "UI、Domain、Data 按职责分层，减少 feature 间耦合。",
                stage = "工程结构"
            )
        )
    }
}
