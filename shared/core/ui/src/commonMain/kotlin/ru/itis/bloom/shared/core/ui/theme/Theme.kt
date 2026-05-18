package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    //TODO("Тёмная тема")
)

private val LightColorScheme = lightColorScheme(
    primary = ColorsCustom.LightPrimary,
    onPrimary = ColorsCustom.LightOnPrimary,
    primaryContainer = ColorsCustom.LightPrimaryContainer,
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