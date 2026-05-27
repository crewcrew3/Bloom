package ru.itis.bloom.shared.core.ui.utils

import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.BurgerMenuSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings

actual fun provideBottomBarSettings(
    onRoutine: () -> Unit,
    onSkinDiary: () -> Unit,
    onMakeupBag: () -> Unit,
    onProfile: () -> Unit
): BottomBarSettings? {
    return BottomBarSettings(
        onRoutineSectionClick = onRoutine,
        onSkinDiarySectionClick = onSkinDiary,
        onMakeupBagSectionClick = onMakeupBag,
        onProfileSectionClick = onProfile
    )
}

actual fun provideTopBarSettings(
    title: String,
    iconType: TopBarIconType,
    onIconClick: () -> Unit
): TopBarSettings {
    // На Android показываем только BACK или NONE
    val androidIconType = if (iconType == TopBarIconType.BURGER) TopBarIconType.NONE else iconType
    return TopBarSettings(
        text = title,
        iconType = androidIconType,
        onIconClick = onIconClick
    )
}

actual fun provideBurgerMenuSettings(
    onRoutine: () -> Unit,
    onSkinDiary: () -> Unit,
    onMakeupBag: () -> Unit,
    onProfile: () -> Unit
): BurgerMenuSettings? {
    // На Android бургер-меню не нужно (есть BottomBar)
    return null
}