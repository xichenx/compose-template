package com.xichen.dev.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val BrandPrimaryLight = Color(0xFF3D5AFE)
val OnBrandPrimaryLight = Color(0xFFFFFFFF)
val BrandPrimaryContainerLight = Color(0xFFE0E7FF)
val OnBrandPrimaryContainerLight = Color(0xFF0F172A)
val BrandSecondaryLight = Color(0xFF0F766E)
val BrandTertiaryLight = Color(0xFF7C3AED)
val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFE2E8F0)
val OnSurfaceLight = Color(0xFF0F172A)
val OnSurfaceVariantLight = Color(0xFF475569)
val OutlineLight = Color(0xFF94A3B8)
val ErrorLight = Color(0xFFB42318)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFEE4E2)
val OnErrorContainerLight = Color(0xFF7A271A)

val BrandPrimaryDark = Color(0xFFB8C4FF)
val OnBrandPrimaryDark = Color(0xFF102A83)
val BrandPrimaryContainerDark = Color(0xFF2539B8)
val OnBrandPrimaryContainerDark = Color(0xFFE0E7FF)
val BrandSecondaryDark = Color(0xFF7DD3C8)
val BrandTertiaryDark = Color(0xFFD8B4FE)
val BackgroundDark = Color(0xFF020617)
val SurfaceDark = Color(0xFF0F172A)
val SurfaceVariantDark = Color(0xFF1E293B)
val OnSurfaceDark = Color(0xFFF8FAFC)
val OnSurfaceVariantDark = Color(0xFFCBD5E1)
val OutlineDark = Color(0xFF64748B)
val ErrorDark = Color(0xFFF97066)
val OnErrorDark = Color(0xFF690F08)
val ErrorContainerDark = Color(0xFF912018)
val OnErrorContainerDark = Color(0xFFFEE4E2)

val SuccessLight = Color(0xFF067647)
val WarningLight = Color(0xFFB54708)
val InfoLight = Color(0xFF175CD3)

val SuccessDark = Color(0xFF6CE9A6)
val WarningDark = Color(0xFFFEC84B)
val InfoDark = Color(0xFF84CAFF)

@Immutable
data class AppSemanticColors(
    val success: Color,
    val warning: Color,
    val info: Color
)

val LocalAppSemanticColors = staticCompositionLocalOf {
    AppSemanticColors(
        success = SuccessLight,
        warning = WarningLight,
        info = InfoLight
    )
}