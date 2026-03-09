package com.xichen.dev.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppMessageDialog(
    title: UiText,
    message: UiText,
    confirmText: UiText,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = {
            Text(text = title.asString())
        },
        text = {
            Text(text = message.asString())
        },
        confirmButton = {
            AppSecondaryButton(
                text = confirmText.asString(),
                onClick = onConfirm
            )
        }
    )
}
