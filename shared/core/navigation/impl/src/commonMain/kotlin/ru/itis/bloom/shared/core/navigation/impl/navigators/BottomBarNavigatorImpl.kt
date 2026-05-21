package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute

class BottomBarNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : BottomBarNavigator {

    override fun toRoutineSection() {
        //backStackHolder.backStack?.add(...)
    }

    override fun toSkinDiarySection() {
        //backStackHolder.backStack?.add(...)
    }

    override fun toMakeupBagSection() {
        backStackHolder.backStack?.add(MakeupBagNavRoute.ProductList)
    }

    override fun toProfileSection() {
        //backStackHolder.backStack?.add(...)
    }
}