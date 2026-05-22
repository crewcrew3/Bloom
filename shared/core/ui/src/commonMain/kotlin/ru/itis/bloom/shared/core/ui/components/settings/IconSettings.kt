package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class IconSettings(
    val iconImageVector: ImageVector? = null, //null т.к. предполагается что есть дефолтная иконка (стрелка назад) но ее сюда я засунуть не могу тк она Composable
    val iconPainter: Painter? = null,
    val color: Color? = null,
    val onClick: () -> Unit = {},
    val description: String = "",
)