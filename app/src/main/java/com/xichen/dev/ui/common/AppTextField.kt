package com.xichen.dev.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorText: UiText? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        singleLine = singleLine,
        isError = errorText != null,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon = {
            leadingContent?.invoke()
        },
        trailingIcon = {
            trailingContent?.invoke()
        },
        supportingText = {
            errorText?.let { Text(text = it.asString()) }
        }
    )
}

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    showPasswordLabel: String,
    hidePasswordLabel: String,
    modifier: Modifier = Modifier,
    errorText: UiText? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingContent: (@Composable () -> Unit)? = null
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        errorText = errorText,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        leadingContent = leadingContent,
        trailingContent = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = if (visible) hidePasswordLabel else showPasswordLabel
                )
            }
        }
    )
}
