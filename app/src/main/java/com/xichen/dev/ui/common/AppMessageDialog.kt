package com.xichen.dev.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.dp
import com.xichen.dev.ui.theme.appElevation
import com.xichen.dev.ui.theme.appRadius
import com.xichen.dev.ui.theme.appSemanticColors
import com.xichen.dev.ui.theme.appSpacing

enum class AppDialogTone {
    Neutral,
    Success,
    Warning,
    Error
}

@Composable
fun AppMessageDialog(
    title: UiText,
    message: UiText,
    confirmText: UiText,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: UiText? = null,
    onDismiss: (() -> Unit)? = null,
    icon: ImageVector? = null,
    tone: AppDialogTone = AppDialogTone.Neutral,
    dismissOnClickOutside: Boolean = true
) {
    val effectiveOnDismiss = onDismiss ?: onConfirm
    val accentColor = when (tone) {
        AppDialogTone.Neutral -> MaterialTheme.colorScheme.primary
        AppDialogTone.Success -> MaterialTheme.appSemanticColors.success
        AppDialogTone.Warning -> MaterialTheme.appSemanticColors.warning
        AppDialogTone.Error -> MaterialTheme.colorScheme.error
    }
    val iconContainerColor = when (tone) {
        AppDialogTone.Neutral -> MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
        AppDialogTone.Success -> MaterialTheme.appSemanticColors.success.copy(alpha = 0.12f)
        AppDialogTone.Warning -> MaterialTheme.appSemanticColors.warning.copy(alpha = 0.12f)
        AppDialogTone.Error -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
    }

    Dialog(
        onDismissRequest = effectiveOnDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Surface(
            modifier = modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(MaterialTheme.appRadius.extraLarge),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = MaterialTheme.appElevation.high,
            shadowElevation = MaterialTheme.appElevation.medium
        ) {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(MaterialTheme.appRadius.extraLarge)
                    )
                    .padding(MaterialTheme.appSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.lg)
            ) {
                if (icon != null) {
                    DialogIconBadge(
                        icon = icon,
                        iconTint = accentColor,
                        containerColor = iconContainerColor
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)
                ) {
                    Text(
                        text = title.asString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message.asString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (dismissText != null) {
                        AppSecondaryButton(
                            text = dismissText.asString(),
                            onClick = effectiveOnDismiss
                        )
                    }
                    AppPrimaryButton(
                        text = confirmText.asString(),
                        onClick = onConfirm,
                        fillMaxWidth = false
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogIconBadge(
    icon: ImageVector,
    iconTint: Color,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = containerColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}
