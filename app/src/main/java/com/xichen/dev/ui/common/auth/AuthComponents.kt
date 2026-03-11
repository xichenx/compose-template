package com.xichen.dev.ui.common.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xichen.dev.R
import com.xichen.dev.ui.common.AppCard
import com.xichen.dev.ui.common.AppPageScaffold
import com.xichen.dev.ui.common.AppPasswordField
import com.xichen.dev.ui.common.AppPrimaryButton
import com.xichen.dev.ui.common.AppSecondaryButton
import com.xichen.dev.ui.common.AppTextField
import com.xichen.dev.ui.common.AppTopBar
import com.xichen.dev.ui.common.UiText
import com.xichen.dev.ui.theme.appElevation
import com.xichen.dev.ui.theme.appRadius
import com.xichen.dev.ui.theme.appSpacing

private val LocalAuthCompactMode = staticCompositionLocalOf { false }

@Composable
fun isAuthCompactMode(): Boolean = LocalAuthCompactMode.current

@Composable
fun AuthScreenScaffold(
    title: String? = null,
    onBackClick: (() -> Unit)?,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    showSupportingPane: Boolean = true,
    supportingContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    AppPageScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            if (title != null) {
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            val isExpandedLayout = maxWidth >= 920.dp
            val isCompact = maxHeight < 860.dp
            val pageMaxWidth = when {
                isExpandedLayout -> 1120.dp
                isCompact -> 520.dp
                else -> 560.dp
            }
            val contentSpacing = if (isCompact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg
            val horizontalPadding = if (isCompact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg
            val verticalPadding = if (isCompact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.xl
            val topContentPadding = if (onBackClick != null) {
                if (isCompact) 56.dp else 68.dp
            } else {
                verticalPadding
            }

            CompositionLocalProvider(LocalAuthCompactMode provides isCompact) {
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 160.dp else 220.dp)
                        .padding(start = 12.dp, top = 16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(if (isCompact) 210.dp else 280.dp)
                        .padding(end = 16.dp, bottom = 12.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            top = topContentPadding,
                            bottom = verticalPadding
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isExpandedLayout && showSupportingPane) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = pageMaxWidth),
                            horizontalArrangement = Arrangement.spacedBy(
                                if (isCompact) MaterialTheme.appSpacing.xl else MaterialTheme.appSpacing.xxl
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(contentSpacing),
                                content = supportingContent
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .widthIn(max = if (isCompact) 480.dp else 520.dp),
                                verticalArrangement = Arrangement.spacedBy(contentSpacing),
                                content = content
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = if (showSupportingPane) pageMaxWidth else 520.dp),
                            verticalArrangement = Arrangement.spacedBy(contentSpacing)
                        ) {
                            if (showSupportingPane) {
                                supportingContent()
                            }
                            content()
                        }
                    }
                }
                if (onBackClick != null) {
                    AuthFloatingBackButton(
                        onBackClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .statusBarsPadding()
                            .padding(
                                start = horizontalPadding,
                                top = if (isCompact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthFloatingBackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        tonalElevation = MaterialTheme.appElevation.medium,
        shadowElevation = MaterialTheme.appElevation.low
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.auth_back)
            )
        }
    }
}

@Composable
fun AuthHeroSection(
    eyebrow: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    highlights: List<String>,
    modifier: Modifier = Modifier
) {
    val compact = isAuthCompactMode()
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.appRadius.extraLarge))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.92f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                shape = RoundedCornerShape(MaterialTheme.appRadius.extraLarge)
            )
            .padding(if (compact) MaterialTheme.appSpacing.lg else MaterialTheme.appSpacing.xl)
    ) {
        val isWideCard = maxWidth >= 420.dp
        val visibleHighlights = if (compact) highlights.take(2) else highlights
        if (isWideCard) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    if (compact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
                    )
                ) {
                    AuthHeroText(
                        eyebrow = eyebrow,
                        title = title,
                        subtitle = subtitle
                    )
                    AuthHighlightChips(highlights = visibleHighlights)
                }
                AuthIllustrationBubble(
                    icon = icon,
                    size = if (compact) 92.dp else 124.dp
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
                )
            ) {
                AuthHeroText(
                    eyebrow = eyebrow,
                    title = title,
                    subtitle = subtitle
                )
                AuthIllustrationBubble(
                    icon = icon,
                    size = if (compact) 84.dp else 112.dp,
                    modifier = Modifier.align(Alignment.End)
                )
                AuthHighlightChips(highlights = visibleHighlights)
            }
        }
    }
}

@Composable
private fun AuthHeroText(
    eyebrow: String,
    title: String,
    subtitle: String
) {
    val compact = isAuthCompactMode()
    Column(
        verticalArrangement = Arrangement.spacedBy(
            if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(MaterialTheme.appRadius.full))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                .padding(
                    horizontal = MaterialTheme.appSpacing.md,
                    vertical = if (compact) MaterialTheme.appSpacing.xs else MaterialTheme.appSpacing.sm
                )
        ) {
            Text(
                text = eyebrow,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = title,
            style = if (compact) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.headlineMedium
            },
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            style = if (compact) {
                MaterialTheme.typography.bodyMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AuthIllustrationBubble(
    icon: ImageVector,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(MaterialTheme.appRadius.extraLarge))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                shape = RoundedCornerShape(MaterialTheme.appRadius.extraLarge)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(size * 0.42f)
        )
    }
}

@Composable
private fun AuthHighlightChips(
    highlights: List<String>,
    modifier: Modifier = Modifier
) {
    val compact = isAuthCompactMode()
    if (highlights.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (compact) MaterialTheme.appSpacing.xs else MaterialTheme.appSpacing.sm
        )
    ) {
        highlights.forEach { item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.appRadius.full))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(MaterialTheme.appRadius.full)
                    )
                    .padding(
                        horizontal = MaterialTheme.appSpacing.md,
                        vertical = if (compact) MaterialTheme.appSpacing.xs else MaterialTheme.appSpacing.sm
                    )
            ) {
                Text(
                    text = item,
                    style = if (compact) {
                        MaterialTheme.typography.labelLarge
                    } else {
                        MaterialTheme.typography.bodyMedium
                    },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AuthFormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val compact = isAuthCompactMode()
    AppCard(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = MaterialTheme.appElevation.medium,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        contentPadding = PaddingValues(if (compact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(
            if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
        ),
        content = content
    )
}

@Composable
fun AuthInfoCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    val compact = isAuthCompactMode()
    val containerColor = if (isError) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
    }
    val contentColor = if (isError) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val titleColor = if (isError) contentColor else MaterialTheme.colorScheme.onSurface

    AppCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = MaterialTheme.appElevation.low,
        contentPadding = PaddingValues(if (compact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(
            if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                if (compact) MaterialTheme.appSpacing.sm else MaterialTheme.appSpacing.md
            )
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(if (compact) 32.dp else 40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isError) {
                                MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)) {
                Text(
                    text = title,
                    style = if (compact) {
                        MaterialTheme.typography.labelLarge
                    } else {
                        MaterialTheme.typography.titleMedium
                    },
                    color = titleColor
                )
                Text(
                    text = body,
                    style = if (compact) {
                        MaterialTheme.typography.labelLarge
                    } else {
                        MaterialTheme.typography.bodyMedium
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AuthFormHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val compact = isAuthCompactMode()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)
    ) {
        Text(
            text = title,
            style = if (compact) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.titleLarge
            },
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = if (compact) {
                MaterialTheme.typography.labelLarge
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AuthSlogan(
    text: String,
    modifier: Modifier = Modifier
) {
    val compact = isAuthCompactMode()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (compact) MaterialTheme.appSpacing.xs else MaterialTheme.appSpacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(MaterialTheme.appRadius.full))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.06f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(MaterialTheme.appRadius.full)
                )
                .padding(
                    horizontal = if (compact) MaterialTheme.appSpacing.md else MaterialTheme.appSpacing.lg,
                    vertical = if (compact) MaterialTheme.appSpacing.xs else MaterialTheme.appSpacing.sm
                )
        ) {
            Text(
                text = text,
                style = if (compact) {
                    MaterialTheme.typography.labelMedium
                } else {
                    MaterialTheme.typography.labelLarge
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AuthCaption(
    text: String,
    modifier: Modifier = Modifier
) {
    val compact = isAuthCompactMode()
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = if (compact) {
            MaterialTheme.typography.labelMedium
        } else {
            MaterialTheme.typography.bodyMedium
        },
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: UiText? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingContent: (@Composable () -> Unit)? = null
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.fillMaxWidth(),
        errorText = errorText,
        keyboardOptions = keyboardOptions,
        leadingContent = leadingContent
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
    errorText: UiText? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingContent: (@Composable () -> Unit)? = null
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
        errorText = errorText,
        keyboardOptions = keyboardOptions,
        leadingContent = leadingContent
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
