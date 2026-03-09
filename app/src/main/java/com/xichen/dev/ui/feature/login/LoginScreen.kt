package com.xichen.dev.ui.feature.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.R
import com.xichen.dev.ui.common.AppMessageDialog
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asString
import com.xichen.dev.ui.common.resolve
import com.xichen.dev.ui.common.auth.AuthFormCard
import com.xichen.dev.ui.common.auth.AuthInfoCard
import com.xichen.dev.ui.common.auth.AuthPasswordField
import com.xichen.dev.ui.common.auth.AuthPrimaryButton
import com.xichen.dev.ui.common.auth.AuthScreenScaffold
import com.xichen.dev.ui.common.auth.AuthSecondaryAction
import com.xichen.dev.ui.common.auth.AuthTextField
import com.xichen.dev.ui.theme.ComposetemplateTheme

@Composable
fun LoginRoute(
    onNavigateBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel, context) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateBack -> onNavigateBack()
                LoginEffect.NavigateToRegister -> onNavigateToRegister()
                LoginEffect.NavigateToHome -> onNavigateToHome()
                is LoginEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.resolve(context))
            }
        }
    }

    LoginScreen(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    AuthScreenScaffold(
        title = null,
        onBackClick = null,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        state.submitError?.let { errorText ->
            AuthInfoCard(
                title = stringResource(R.string.login_error_title),
                body = errorText.asString(),
                isError = true
            )
        }
        AuthFormCard {
            AuthTextField(
                value = state.email,
                onValueChange = { onIntent(LoginIntent.EmailChanged(it)) },
                label = stringResource(R.string.login_email_label),
                errorText = state.emailError
            )
            AuthPasswordField(
                value = state.password,
                onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                label = stringResource(R.string.login_password_label),
                visible = state.isPasswordVisible,
                onToggleVisibility = { onIntent(LoginIntent.TogglePasswordVisibility) },
                errorText = state.passwordError
            )
            AuthPrimaryButton(
                text = stringResource(R.string.login_submit),
                loading = state.isSubmitting,
                enabled = state.canSubmit,
                onClick = { onIntent(LoginIntent.Submit) }
            )
            AuthSecondaryAction(
                text = stringResource(R.string.login_open_register),
                onClick = { onIntent(LoginIntent.OpenRegister) }
            )
        }
    }

    state.successMessage?.let { successMessage ->
        LoginSuccessDialog(
            message = successMessage,
            onConfirm = { onIntent(LoginIntent.DismissSuccess) }
        )
    }
}

@Composable
private fun LoginSuccessDialog(
    message: UiText,
    onConfirm: () -> Unit
) {
    AppMessageDialog(
        title = UiText.StringResource(R.string.login_success_title),
        message = message,
        confirmText = UiText.StringResource(R.string.login_success_confirm),
        onConfirm = onConfirm
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ComposetemplateTheme {
        LoginScreen(
            state = LoginState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
