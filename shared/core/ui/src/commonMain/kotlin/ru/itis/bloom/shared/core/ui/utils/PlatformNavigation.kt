package ru.itis.bloom.shared.core.ui.utils

import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.BurgerMenuSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings

expect fun provideBottomBarSettings(
    onRoutine: () -> Unit,
    onSkinDiary: () -> Unit,
    onMakeupBag: () -> Unit,
    onProfile: () -> Unit
): BottomBarSettings?

expect fun provideTopBarSettings(
    title: String,
    iconType: TopBarIconType,
    onIconClick: () -> Unit
): TopBarSettings

expect fun provideBurgerMenuSettings(
    onRoutine: () -> Unit,
    onSkinDiary: () -> Unit,
    onMakeupBag: () -> Unit,
    onProfile: () -> Unit
): BurgerMenuSettings?