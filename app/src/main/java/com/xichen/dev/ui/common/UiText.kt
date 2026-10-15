package com.xichen.dev.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    data object Empty : UiText()

    data class DynamicString(
        val value: String
    ) : UiText()

    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText()
}

fun UiText.resolve(context: Context): String {
    return when (this) {
        UiText.Empty -> ""
        is UiText.DynamicString -> value
        is UiText.StringResource -> context.getString(resId, *args.toTypedArray())
    }
}

@Composable
fun UiText.asString(): String {
    val context = LocalContext.current
    return resolve(context)
}
