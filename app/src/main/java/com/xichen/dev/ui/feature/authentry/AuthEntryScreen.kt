package com.xichen.dev.ui.feature.authentry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xichen.dev.R
import com.xichen.dev.ui.common.AppCard
import com.xichen.dev.ui.common.auth.AuthFormCard
import com.xichen.dev.ui.common.auth.AuthFormHeader
import com.xichen.dev.ui.common.auth.AuthHeroSection
import com.xichen.dev.ui.common.auth.AuthScreenScaffold
import com.xichen.dev.ui.common.auth.isAuthCompactMode
import com.xichen.dev.ui.theme.ComposetemplateTheme
import com.xichen.dev.ui.theme.appSpacing

@Composable
fun AuthEntryRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthEntryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AuthEntryEffect.NavigateToLogin -> onNavigateToLogin()
                AuthEntryEffect.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    AuthEntryScreen(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun AuthEntryScreen(
    state: AuthEntryState,
    onIntent: (AuthEntryIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    AuthScreenScaffold(
        title = null,
        onBackClick = null,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        supportingContent = {
            val compact = isAuthCompactMode()
            AuthHeroSection(
                eyebrow = stringResource(R.string.auth_entry_eyebrow),
                title = stringResource(R.string.auth_entry_title),
                subtitle = stringResource(
                    if (compact) {
                        R.string.auth_entry_subtitle_compact
                    } else {
                        R.string.auth_entry_subtitle
                    }
                ),
                icon = Icons.Outlined.Badge,
                highlights = listOf(
                    stringResource(R.string.auth_entry_highlight_fast)
                )
            )
        }
    ) {
        val compact = isAuthCompactMode()
        AuthFormCard {
            AuthFormHeader(
                title = stringResource(R.string.auth_entry_form_title),
                description = stringResource(R.string.auth_entry_form_description)
            )
            AuthEntrySplitCards(
                compact = compact,
                enabled = !state.isLoading,
                onOpenLogin = { onIntent(AuthEntryIntent.OpenLogin) },
                onOpenRegister = { onIntent(AuthEntryIntent.OpenRegister) }
            )
        }
    }
}

@Composable
private fun AuthEntrySplitCards(
    compact: Boolean,
    enabled: Boolean,
    onOpenLogin: () -> Unit,
    onOpenRegister: () -> Unit
) {
    if (!compact) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)
        ) {
            AuthEntryOptionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.auth_entry_login_title),
                description = stringResource(R.string.auth_entry_login_description),
                actionText = stringResource(R.string.auth_entry_login_action),
                badgeText = stringResource(R.string.auth_entry_login_badge),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                accentColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                enabled = enabled,
                onClick = onOpenLogin,
                compact = compact
            )
            AuthEntryOptionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.PersonAddAlt1,
                title = stringResource(R.string.auth_entry_register_title),
                description = stringResource(R.string.auth_entry_register_description),
                actionText = stringResource(R.string.auth_entry_register_action),
                badgeText = stringResource(R.string.auth_entry_register_badge),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                accentColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                enabled = enabled,
                onClick = onOpenRegister,
                compact = compact
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
            AuthEntryOptionCard(
                icon = Icons.Outlined.Lock,
                title = stringResource(R.string.auth_entry_login_title),
                description = stringResource(R.string.auth_entry_login_description),
                actionText = stringResource(R.string.auth_entry_login_action),
                badgeText = stringResource(R.string.auth_entry_login_badge),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                accentColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                enabled = enabled,
                onClick = onOpenLogin,
                compact = compact
            )
            AuthEntryOptionCard(
                icon = Icons.Outlined.PersonAddAlt1,
                title = stringResource(R.string.auth_entry_register_title),
                description = stringResource(R.string.auth_entry_register_description),
                actionText = stringResource(R.string.auth_entry_register_action),
                badgeText = stringResource(R.string.auth_entry_register_badge),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                accentColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                enabled = enabled,
                onClick = onOpenRegister,
                compact = compact
            )
        }
    }
}

@Composable
private fun AuthEntryOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    actionText: String,
    badgeText: String,
    containerColor: Color,
    accentColor: Color,
    contentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    AppCard(
        modifier = modifier,
        onClick = { if (enabled) onClick() },
        containerColor = containerColor,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            if (compact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg
        ),
        verticalArrangement = Arrangement.spacedBy(
            if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (compact) 42.dp else 50.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = accentColor.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(
                        horizontal = MaterialTheme.appSpacing.sm,
                        vertical = MaterialTheme.appSpacing.xs
                    )
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)) {
            Text(
                text = title,
                style = if (compact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                color = contentColor
            )
            Text(
                text = description,
                style = if (compact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
                color = contentColor.copy(alpha = 0.80f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.34f),
                    shape = RoundedCornerShape(MaterialTheme.appSpacing.md)
                )
                .padding(
                    horizontal = MaterialTheme.appSpacing.md,
                    vertical = MaterialTheme.appSpacing.sm
                )
        ) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthEntryScreenPreview() {
    ComposetemplateTheme {
        AuthEntryScreen(
            state = AuthEntryState(),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
