package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class FloatingActionButtonSettings(
    val onClick: () -> Unit = {},
    val contentColor: Color? = null,
    val containerColor: Color? = null,
    val shape: Shape? = null,
    val iconSettings: IconSettings,
)