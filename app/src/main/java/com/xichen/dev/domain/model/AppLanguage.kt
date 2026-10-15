package com.xichen.dev.domain.model

enum class AppLanguage(
    val storageKey: String,
    val languageTag: String?
) {
    FollowSystem(
        storageKey = "system",
        languageTag = null
    ),
    English(
        storageKey = "en",
        languageTag = "en"
    ),
    SimplifiedChinese(
        storageKey = "zh-CN",
        languageTag = "zh-CN"
    );

    companion object {
        val default: AppLanguage = English

        fun fromStorageKey(value: String?): AppLanguage {
            return entries.firstOrNull { language -> language.storageKey == value } ?: default
        }
    }
}
