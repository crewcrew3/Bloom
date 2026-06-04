package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ColorsCustom.DarkPrimary,
    onPrimary = ColorsCustom.DarkOnPrimary,
    primaryContainer = ColorsCustom.DarkPrimaryContainer,
    secondary = ColorsCustom.DarkSecondary,
    secondaryContainer = ColorsCustom.DarkSecondaryContainer,
    onSecondaryContainer = ColorsCustom.DarkOnSecondaryContainer,
    tertiary = ColorsCustom.DarkTertiary,
    tertiaryContainer = ColorsCustom.DarkTertiaryContainer,
    background = ColorsCustom.DarkBackground,
    error = ColorsCustom.DarkError,
    onError = ColorsCustom.DarkOnError,
    onSurface = ColorsCustom.DarkOnSurface,
    surfaceVariant = ColorsCustom.DarkSurfaceVariant,
    onSurfaceVariant = ColorsCustom.DarkOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = ColorsCustom.LightPrimary,
    onPrimary = ColorsCustom.LightOnPrimary,
    primaryContainer = ColorsCustom.LightPrimaryContainer,
    onPrimaryContainer = ColorsCustom.LightOnPrimaryContainer,
    secondary = ColorsCustom.LightSecondary,
    onSecondary = ColorsCustom.LightOnSecondary,
    secondaryContainer = ColorsCustom.LightSecondaryContainer,
    onSecondaryContainer = ColorsCustom.LightOnSecondaryContainer,
    tertiary = ColorsCustom.LightTertiary,
    onTertiary = ColorsCustom.LightOnTertiary,
    tertiaryContainer = ColorsCustom.LightTertiaryContainer,
    background = ColorsCustom.LightBackground,
    onBackground = ColorsCustom.LightOnBackground,
    error = ColorsCustom.LightError,
    onError = ColorsCustom.LightOnError,
    errorContainer = ColorsCustom.LightErrorContainer,
    surface = ColorsCustom.LightSurface,
    onSurface = ColorsCustom.LightOnSurface,
)

@Composable
fun BloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}