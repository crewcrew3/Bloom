package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder

class BottomBarNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : BottomBarNavigator {

    override fun toRoutineSection() {
        //backStackHolder.backStack?.add(...)
    }

    override fun toSkinDiarySection() {
        //backStackHolder.backStack?.add(...)
    }

    override fun toSkinMakeupBagSection() {
        //backStackHolder.backStack?.add(...)
    }

    override fun toProfileSection() {
        //backStackHolder.backStack?.add(...)
    }
}