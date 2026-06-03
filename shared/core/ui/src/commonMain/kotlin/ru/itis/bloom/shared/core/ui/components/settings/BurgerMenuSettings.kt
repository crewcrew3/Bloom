package ru.itis.bloom.shared.core.ui.components.settings

data class BurgerMenuSettings(
    val onRoutineClick: () -> Unit,
    val onSkinDiaryClick: () -> Unit,
    val onMakeupBagClick: () -> Unit,
    val onProfileClick: () -> Unit
)