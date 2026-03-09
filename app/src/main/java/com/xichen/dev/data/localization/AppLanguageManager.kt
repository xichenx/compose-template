package com.xichen.dev.data.localization

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.core.os.ConfigurationCompat
import com.xichen.dev.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLanguageManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    fun wrapContext(base: Context, language: AppLanguage): Context {
        if (language == AppLanguage.FollowSystem) {
            Locale.setDefault(resolveLocale(base, language))
            return base
        }

        val locale = resolveLocale(base, language)
        Locale.setDefault(locale)

        val configuration = Configuration(base.resources.configuration).apply {
            setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocales(android.os.LocaleList(locale))
            }
        }
        return base.createConfigurationContext(configuration)
    }

    fun applyLanguage(language: AppLanguage) {
        val locale = resolveLocale(appContext, language)
        Locale.setDefault(locale)

        val configuration = Configuration(appContext.resources.configuration).apply {
            setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocales(android.os.LocaleList(locale))
            }
        }
        @Suppress("DEPRECATION")
        appContext.resources.updateConfiguration(configuration, appContext.resources.displayMetrics)
        @Suppress("DEPRECATION")
        appContext.applicationContext.resources.updateConfiguration(
            configuration,
            appContext.applicationContext.resources.displayMetrics
        )
    }

    private fun resolveLocale(context: Context, language: AppLanguage): Locale {
        return when (language) {
            AppLanguage.FollowSystem ->
                ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
                    ?: ConfigurationCompat.getLocales(context.resources.configuration)[0]
                    ?: ConfigurationCompat.getLocales(
                        context.applicationContext.resources.configuration
                    )[0]
                    ?: Locale.getDefault()
            else -> Locale.forLanguageTag(language.languageTag.orEmpty())
        }
    }
}
