package com.xichen.dev.ui.feature.register

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.R
import com.xichen.dev.ui.common.AppMessageDialog
import com.xichen.dev.ui.common.AppDialogTone
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.common.asString
import com.xichen.dev.ui.common.resolve
import com.xichen.dev.ui.common.auth.AuthFormCard
import com.xichen.dev.ui.common.auth.AuthFormHeader
import com.xichen.dev.ui.common.auth.AuthInfoCard
import com.xichen.dev.ui.common.auth.AuthPasswordField
import com.xichen.dev.ui.common.auth.AuthPrimaryButton
import com.xichen.dev.ui.common.auth.AuthScreenScaffold
import com.xichen.dev.ui.common.auth.AuthSlogan
import com.xichen.dev.ui.common.auth.AuthTextField
import com.xichen.dev.ui.theme.ComposetemplateTheme

@Composable
fun RegisterRoute(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel, context) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterEffect.NavigateBack -> onNavigateBack()
                RegisterEffect.NavigateToHome -> onNavigateToHome()
                is RegisterEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message.resolve(context))
            }
        }
    }

    RegisterScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onIntent: (RegisterIntent) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    AuthScreenScaffold(
        title = null,
        onBackClick = onNavigateBack,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        showSupportingPane = false
    ) {
        state.submitError?.let { errorText ->
            AuthInfoCard(
                title = stringResource(R.string.register_error_title),
                body = errorText.asString(),
                isError = true,
                leadingIcon = Icons.Outlined.TipsAndUpdates
            )
        }
        AuthSlogan(text = stringResource(R.string.register_slogan))
        AuthFormCard {
            AuthFormHeader(
                title = stringResource(R.string.register_form_title),
                description = stringResource(R.string.register_form_description)
            )
            AuthTextField(
                value = state.displayName,
                onValueChange = { onIntent(RegisterIntent.DisplayNameChanged(it)) },
                label = stringResource(R.string.register_name_label),
                errorText = state.displayNameError,
                leadingContent = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Badge,
                        contentDescription = null
                    )
                }
            )
            AuthTextField(
                value = state.email,
                onValueChange = { onIntent(RegisterIntent.EmailChanged(it)) },
                label = stringResource(R.string.register_email_label),
                errorText = state.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingContent = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.AlternateEmail,
                        contentDescription = null
                    )
                }
            )
            AuthPasswordField(
                value = state.password,
                onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
                label = stringResource(R.string.register_password_label),
                visible = state.isPasswordVisible,
                onToggleVisibility = { onIntent(RegisterIntent.TogglePasswordVisibility) },
                errorText = state.passwordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingContent = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                }
            )
            AuthPasswordField(
                value = state.confirmPassword,
                onValueChange = { onIntent(RegisterIntent.ConfirmPasswordChanged(it)) },
                label = stringResource(R.string.register_confirm_password_label),
                visible = state.isConfirmPasswordVisible,
                onToggleVisibility = { onIntent(RegisterIntent.ToggleConfirmPasswordVisibility) },
                errorText = state.confirmPasswordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingContent = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                }
            )
            AuthPrimaryButton(
                text = stringResource(R.string.register_submit),
                loading = state.isSubmitting,
                enabled = state.canSubmit,
                onClick = { onIntent(RegisterIntent.Submit) }
            )
        }
    }

    state.successMessage?.let { successMessage ->
        RegisterSuccessDialog(
            message = successMessage,
            onConfirm = { onIntent(RegisterIntent.DismissSuccess) }
        )
    }
}

@Composable
private fun RegisterSuccessDialog(
    message: UiText,
    onConfirm: () -> Unit
) {
    AppMessageDialog(
        title = UiText.StringResource(R.string.register_success_title),
        message = message,
        confirmText = UiText.StringResource(R.string.register_success_confirm),
        onConfirm = onConfirm,
        icon = Icons.Outlined.CheckCircle,
        tone = AppDialogTone.Success
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    ComposetemplateTheme {
        RegisterScreen(
            state = RegisterState(),
            onIntent = {},
            onNavigateBack = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
