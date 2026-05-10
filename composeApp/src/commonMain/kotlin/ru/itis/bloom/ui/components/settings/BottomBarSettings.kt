package ru.itis.bloom.ui.components.settings

data class BottomBarSettings(
    val onRoutineSectionClick: () -> Unit,
    val onSkinDiarySectionClick: () -> Unit,
    val onMakeupBagSectionClick: () -> Unit,
    val onProfileSectionClick: () -> Unit,
)