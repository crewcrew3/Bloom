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
    // На Desktop не показываем BottomBar
    return null
}

actual fun provideTopBarSettings(
    title: String,
    iconType: TopBarIconType,
    onIconClick: () -> Unit
): TopBarSettings {
    // На Desktop показываем BURGER или BACK
    val desktopIconType = if (iconType == TopBarIconType.NONE) TopBarIconType.BURGER else iconType
    return TopBarSettings(
        text = title,
        iconType = desktopIconType,
        onIconClick = onIconClick
    )
}

actual fun provideBurgerMenuSettings(
    onRoutine: () -> Unit,
    onSkinDiary: () -> Unit,
    onMakeupBag: () -> Unit,
    onProfile: () -> Unit
): BurgerMenuSettings? {
    return BurgerMenuSettings(
        onRoutineClick = onRoutine,
        onSkinDiaryClick = onSkinDiary,
        onMakeupBagClick = onMakeupBag,
        onProfileClick = onProfile
    )
}