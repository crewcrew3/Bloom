package ru.itis.bloom.shared.core.ui.components.settings

data class BottomBarSettings(
    val onRoutineSectionClick: () -> Unit,
    val onSkinDiarySectionClick: () -> Unit,
    val onMakeupBagSectionClick: () -> Unit,
    val onProfileSectionClick: () -> Unit,
)