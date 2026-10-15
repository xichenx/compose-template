package com.xichen.dev.ui.common.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xichen.dev.R
import com.xichen.dev.ui.feature.register.RegisterScreen
import com.xichen.dev.ui.feature.register.RegisterState
import com.xichen.dev.ui.theme.ComposetemplateTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenScaffoldTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun registrationFormCanScrollInShortViewport() {
        composeRule.setContent {
            ComposetemplateTheme(dynamicColor = false) {
                Box(Modifier.fillMaxWidth().height(320.dp)) {
                    RegisterScreen(
                        state = RegisterState(),
                        onIntent = {},
                        onNavigateBack = {},
                        snackbarHostState = remember { SnackbarHostState() }
                    )
                }
            }
        }
        val submitText = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.register_submit)
        composeRule.onAllNodes(hasClickAction() and hasText(submitText))
            .onLast()
            .performScrollTo()
            .assertIsDisplayed()
    }
}
