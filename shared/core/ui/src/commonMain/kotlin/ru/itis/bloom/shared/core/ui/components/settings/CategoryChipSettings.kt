package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color

data class CategoryChipSettings(
    val text: String,
    val isSelected: Boolean = false,
    val onClick: () -> Unit = {},
    val selectedColor: Color? = null,
    val unselectedBorderColor: Color? = null,
    val textColor: Color? = null
)