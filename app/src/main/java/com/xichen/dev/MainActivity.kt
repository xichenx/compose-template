package com.xichen.dev

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.data.local.datastore.LanguagePreferencesDataSource
import com.xichen.dev.data.localization.AppLanguageManager
import com.xichen.dev.ui.navigation.AppNavHost
import com.xichen.dev.ui.theme.ComposetemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val appLanguageManager = AppLanguageManager(newBase.applicationContext)
        val language = runBlocking {
            LanguagePreferencesDataSource.read(newBase.applicationContext)
        }
        super.attachBaseContext(appLanguageManager.wrapContext(newBase, language))
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            val windowSizeClass = calculateWindowSizeClass(this)
            ComposetemplateTheme(themeMode = state.themeMode) {
                AppNavHost(windowSizeClass = windowSizeClass)
            }
        }
    }
}
