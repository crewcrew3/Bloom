package ru.itis.bloom.ui.components.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class IconSettings(
    val icon: ImageVector? = null, //null т.к. предполагается что есть дефолтная иконка (стрелка назад) но ее сюда я засунуть не могу тк она Composable
    val color: Color? = null,
    val onClick: () -> Unit = {},
    val description: String = "",
)