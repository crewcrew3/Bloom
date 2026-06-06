package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.StringResource

data class DrawerMenuItem(
    val icon: Painter,
    val titleRes: StringResource,
    val onClick: () -> Unit
)