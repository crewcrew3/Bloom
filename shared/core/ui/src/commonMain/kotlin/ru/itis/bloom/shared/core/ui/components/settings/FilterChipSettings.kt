package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.itis.bloom.shared.core.ui.theme.BloomTheme

data class FilterChipSettings(
    val text: String,
    val isSelected: Boolean = false,
    val onClick: () -> Unit = {},
    val selectedColor: Color? = null,
    val unselectedColor: Color? = null,
    val textColor: Color? = null
) {
    @Composable
    fun resolveSelectedColor(): Color =
        selectedColor ?: MaterialTheme.colorScheme.primaryContainer

    @Composable
    fun resolveUnselectedColor(): Color =
        unselectedColor ?: MaterialTheme.colorScheme.surfaceVariant

    @Composable
    fun resolveTextColor(): Color =
        textColor ?: (if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
}