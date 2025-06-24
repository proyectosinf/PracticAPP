package com.mobivery.fct25.app.common.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MaterialColors(
    // Material 3 default colors
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onTertiary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val error: Color,
    val outline: Color,
    // Custom colors
    val primaryVariant: Color,
    val onPrimaryVariant: Color,
    val warning: Color,
    val success: Color,
    val onBackgroundSecondary: Color,
    val onSurfaceSecondary: Color,
    val backgroundVariant: Color,
    val onBackgroundVariant: Color,
    val backgroundVariant2: Color,
    val onBackgroundVariant2: Color
)
