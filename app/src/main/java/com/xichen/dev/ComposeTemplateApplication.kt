package com.xichen.dev

import android.app.Application
import com.xichen.dev.domain.usecase.InitializeAppLanguageUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class ComposeTemplateApplication : Application() {

    @Inject
    lateinit var initializeAppLanguageUseCase: InitializeAppLanguageUseCase

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            initializeAppLanguageUseCase()
        }
    }
}
