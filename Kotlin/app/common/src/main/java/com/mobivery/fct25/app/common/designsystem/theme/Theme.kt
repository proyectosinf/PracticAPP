package com.mobivery.fct25.app.common.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val AppColors: MaterialColors
    @Composable
    get() = LocalMaterialColors.current

val AppTypographies = Typography()

private val LocalMaterialColors = staticCompositionLocalOf<MaterialColors> {
    error("MaterialColors is not provided. Make sure to wrap your content in AppTheme.")
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColorScheme = if (darkTheme) CustomDarkColorScheme else CustomLightColorScheme

    val materialColors = MaterialColors(
        primary = colorScheme.primary,
        secondary = colorScheme.secondary,
        tertiary = colorScheme.tertiary,
        background = colorScheme.background,
        surface = colorScheme.surface,
        onPrimary = colorScheme.onPrimary,
        onSecondary = colorScheme.onSecondary,
        onTertiary = colorScheme.onTertiary,
        onBackground = colorScheme.onBackground,
        onSurface = colorScheme.onSurface,
        primaryContainer = colorScheme.primaryContainer,
        onPrimaryContainer = colorScheme.onPrimaryContainer,
        error = colorScheme.error,
        outline = colorScheme.outline,
        // Custom colors
        primaryVariant = customColorScheme.primaryVariant,
        onPrimaryVariant = customColorScheme.onPrimaryVariant,
        warning = customColorScheme.warning,
        success = customColorScheme.success,
        onBackgroundSecondary = customColorScheme.onBackgroundSecondary,
        onSurfaceSecondary = customColorScheme.onSurfaceSecondary,
        backgroundVariant = customColorScheme.backgroundVariant,
        onBackgroundVariant = customColorScheme.onBackgroundVariant,
        backgroundVariant2 = customColorScheme.backgroundVariant2,
        onBackgroundVariant2 = customColorScheme.onBackgroundVariant2
    )

    CompositionLocalProvider(
        LocalMaterialColors provides materialColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
