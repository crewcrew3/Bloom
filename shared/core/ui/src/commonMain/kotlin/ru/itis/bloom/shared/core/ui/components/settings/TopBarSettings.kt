package ru.itis.bloom.shared.core.ui.components.settings

import androidx.compose.ui.graphics.Color

enum class TopBarIconType {
    NONE,      // Нет иконки
    BACK,      // Стрелка назад
    BURGER     // Бургер-меню
}

data class TopBarSettings(
    val text: String,
    val textColor: Color? = null,
    val containerColor: Color? = null,
    val iconType: TopBarIconType = TopBarIconType.NONE,
    val onIconClick: () -> Unit = {}
)