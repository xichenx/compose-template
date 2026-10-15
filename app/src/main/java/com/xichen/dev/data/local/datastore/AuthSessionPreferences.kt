package com.xichen.dev.data.local.datastore

data class AuthSessionPreferences(
    val userId: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val token: String? = null
) {
    val isLoggedIn: Boolean
        get() = !token.isNullOrBlank()
}
