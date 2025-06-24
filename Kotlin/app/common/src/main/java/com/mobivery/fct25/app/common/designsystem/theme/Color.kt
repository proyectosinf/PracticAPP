package com.mobivery.fct25.app.common.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Schemes
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2856ED),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF8AABF5),
    onPrimaryContainer = Color(0xFF68478E),
    secondary = Color(0xFF02B09E),
    tertiary = Color(0xFFB2675E),
    background = Color(0xFFF7F7F7),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    error = Color(0xFFDC362E),
    outline = Color(0xFFDDDDDD),
)

internal val CustomLightColorScheme = CustomColorScheme(
    primaryVariant = Color(0xFF3E2B55),
    onPrimaryVariant = Color(0xFFFFFFFF),
    warning = Color(0xFFEFA400),
    success = Color(0xFF007D00),
    onBackgroundSecondary = Color(0xFF575757),
    onSurfaceSecondary = Color(0xFF575757),
    backgroundVariant = Color(0xFFBCBCBC),
    onBackgroundVariant = Color(0xFF797979),
    backgroundVariant2 = Color(0xFFF7F7F7),
    onBackgroundVariant2 = Color(0xFF797979)
)

// Dark schemes
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA491BB),
    onPrimary = Color(0xFF150E1C),
    primaryContainer = Color(0xFF533972),
    onPrimaryContainer = Color(0xFFE1DAE8),
    secondary = Color(0xFF02B09E),
    tertiary = Color(0xFFB2675E),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF7F7F7),
    surface = Color(0xFF232323),
    onSurface = Color(0xFFF7F7F7),
    error = Color(0xFFE46962),
    outline = Color(0xFF343434),
)

internal val CustomDarkColorScheme = CustomColorScheme(
    primaryVariant = Color(0xFF3E2B55),
    onPrimaryVariant = Color(0xFF150E1C),
    warning = Color(0xFFF2B633),
    success = Color(0xFF007D00),
    onBackgroundSecondary = Color(0xFF9A9A9A),
    onSurfaceSecondary = Color(0xFF9A9A9A),
    backgroundVariant = Color(0xFF464646),
    onBackgroundVariant = Color(0xFF797979),
    backgroundVariant2 = Color(0xFF232323),
    onBackgroundVariant2 = Color(0xFF797979)
)
