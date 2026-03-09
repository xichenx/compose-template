package com.xichen.dev.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.xichen.dev.domain.model.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = OnBrandPrimaryDark,
    primaryContainer = BrandPrimaryContainerDark,
    onPrimaryContainer = OnBrandPrimaryContainerDark,
    secondary = BrandSecondaryDark,
    tertiary = BrandTertiaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimaryLight,
    onPrimary = OnBrandPrimaryLight,
    primaryContainer = BrandPrimaryContainerLight,
    onPrimaryContainer = OnBrandPrimaryContainerLight,
    secondary = BrandSecondaryLight,
    tertiary = BrandTertiaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

@Composable
fun ComposetemplateTheme(
    themeMode: AppThemeMode = AppThemeMode.default,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme = when (themeMode) {
        AppThemeMode.FollowSystem -> isSystemInDarkTheme()
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val semanticColors = if (darkTheme) {
        AppSemanticColors(
            success = SuccessDark,
            warning = WarningDark,
            info = InfoDark
        )
    } else {
        AppSemanticColors(
            success = SuccessLight,
            warning = WarningLight,
            info = InfoLight
        )
    }
    val spacing = AppSpacing()
    val radius = AppRadius()
    val elevation = AppElevation()
    val view = context.findActivity()

    if (view != null) {
        SideEffect {
            val window = view.window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppSemanticColors provides semanticColors,
        LocalAppSpacing provides spacing,
        LocalAppRadius provides radius,
        LocalAppElevation provides elevation
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val MaterialTheme.appSemanticColors: AppSemanticColors
    @Composable
    get() = LocalAppSemanticColors.current

val MaterialTheme.appSpacing: AppSpacing
    @Composable
    get() = LocalAppSpacing.current

val MaterialTheme.appRadius: AppRadius
    @Composable
    get() = LocalAppRadius.current

val MaterialTheme.appElevation: AppElevation
    @Composable
    get() = LocalAppElevation.current

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}