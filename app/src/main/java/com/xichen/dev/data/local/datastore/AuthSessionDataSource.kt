package com.xichen.dev.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.xichen.dev.domain.model.LoginSession
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authSessionDataStore by preferencesDataStore(name = "auth_session")

class AuthSessionDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getSession(): AuthSessionPreferences {
        return context.authSessionDataStore.data.map(::toSessionPreferences).first()
    }

    suspend fun saveSession(session: LoginSession) {
        context.authSessionDataStore.edit { preferences ->
            preferences[Keys.UserId] = session.userId
            preferences[Keys.DisplayName] = session.displayName
            preferences[Keys.Email] = session.email
            preferences[Keys.Token] = session.token
        }
    }

    suspend fun clearSession() {
        context.authSessionDataStore.edit { preferences ->
            preferences.remove(Keys.UserId)
            preferences.remove(Keys.DisplayName)
            preferences.remove(Keys.Email)
            preferences.remove(Keys.Token)
        }
    }

    private fun toSessionPreferences(preferences: Preferences): AuthSessionPreferences {
        return AuthSessionPreferences(
            userId = preferences[Keys.UserId],
            displayName = preferences[Keys.DisplayName],
            email = preferences[Keys.Email],
            token = preferences[Keys.Token]
        )
    }

    private object Keys {
        val UserId = stringPreferencesKey("user_id")
        val DisplayName = stringPreferencesKey("display_name")
        val Email = stringPreferencesKey("email")
        val Token = stringPreferencesKey("token")
    }
}
