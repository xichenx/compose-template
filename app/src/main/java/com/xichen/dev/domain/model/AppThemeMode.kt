package com.xichen.dev.domain.model

enum class AppThemeMode(
    val storageKey: String
) {
    FollowSystem("system"),
    Light("light"),
    Dark("dark");

    companion object {
        val default: AppThemeMode = FollowSystem

        fun fromStorageKey(value: String?): AppThemeMode {
            return entries.firstOrNull { mode -> mode.storageKey == value } ?: default
        }
    }
}
