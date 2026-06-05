package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.profile.api.navigation.ProfileNavRoute
import ru.itis.bloom.shared.feature.skindiary.api.navigation.DiaryNavRoute

class BottomBarNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : BottomBarNavigator {

    override fun toRoutineSection() {
        //backStackHolder.navigateToRoot(...)
    }

    override fun toSkinDiarySection() {
        backStackHolder.navigateToRoot(DiaryNavRoute.List)
    }

    override fun toMakeupBagSection() {
        backStackHolder.navigateToRoot(MakeupBagNavRoute.ProductList)
    }

    override fun toProfileSection() {
        backStackHolder.navigateToRoot(ProfileNavRoute.Profile)
    }
}