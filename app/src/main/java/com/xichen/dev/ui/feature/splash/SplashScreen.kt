package com.xichen.dev.ui.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.ui.common.ErrorContent
import com.xichen.dev.ui.common.asString
import com.xichen.dev.ui.theme.ComposetemplateTheme

@Composable
fun SplashRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.CheckSession)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SplashEffect.NavigateToLogin -> onNavigateToLogin()
                SplashEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    SplashScreen(
        state = state,
        modifier = modifier,
        onRetry = { viewModel.onIntent(SplashIntent.CheckSession) }
    )
}

@Composable
fun SplashScreen(
    state: SplashState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    state.errorMessage?.let { error ->
        ErrorContent(message = error, onRetry = onRetry, modifier = modifier.fillMaxSize())
        return
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = state.title.asString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = state.subtitle.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    ComposetemplateTheme {
        SplashScreen(state = SplashState())
    }
}
