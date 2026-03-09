package com.xichen.dev.ui.common.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.xichen.dev.R
import com.xichen.dev.ui.common.AppCard
import com.xichen.dev.ui.common.AppInfoCard
import com.xichen.dev.ui.common.AppListContainer
import com.xichen.dev.ui.common.AppPageScaffold
import com.xichen.dev.ui.common.AppPasswordField
import com.xichen.dev.ui.common.AppPrimaryButton
import com.xichen.dev.ui.common.AppSecondaryButton
import com.xichen.dev.ui.common.AppTextField
import com.xichen.dev.ui.common.AppTopBar
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.theme.appElevation
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AuthScreenScaffold(
    title: String? = null,
    onBackClick: (() -> Unit)?,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    AppPageScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            if (title != null || onBackClick != null) {
                AppTopBar(
                    title = title,
                    navigationText = if (onBackClick != null) {
                        stringResource(R.string.auth_back)
                    } else {
                        null
                    },
                    onNavigationClick = onBackClick
                )
            }
        }
    ) { innerPadding ->
        AppListContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalItemSpacing = MaterialTheme.appSpacing.xl
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.lg),
                    content = content
                )
            }
        }        
    }
}

@Composable
fun AuthHeroSection(
    eyebrow: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = MaterialTheme.appSpacing.sm)) {
        Text(
            text = eyebrow,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = MaterialTheme.appSpacing.sm)
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MaterialTheme.appSpacing.sm)
        )
    }
}

@Composable
fun AuthFormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = MaterialTheme.appElevation.medium,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
        content = content
    )
}

@Composable
fun AuthInfoCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    val containerColor = if (isError) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = if (isError) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    AppInfoCard(
        title = title,
        description = body,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        titleColor = contentColor
    )
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: UiText? = null
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.fillMaxWidth(),
        errorText = errorText
    )
}

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    errorText: UiText? = null
) {
    AppPasswordField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        visible = visible,
        onToggleVisibility = onToggleVisibility,
        showPasswordLabel = stringResource(R.string.auth_show_password),
        hidePasswordLabel = stringResource(R.string.auth_hide_password),
        modifier = modifier.fillMaxWidth(),
        errorText = errorText
    )
}

@Composable
fun AuthPrimaryButton(
    text: String,
    loading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading
    )
}

@Composable
fun AuthSecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppSecondaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        fillMaxWidth = true
    )
}
