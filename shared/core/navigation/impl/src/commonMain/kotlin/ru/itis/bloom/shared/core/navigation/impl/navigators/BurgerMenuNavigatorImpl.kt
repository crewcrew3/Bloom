package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.BurgerMenuNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.skindiary.api.navigation.DiaryNavRoute

class BurgerMenuNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : BurgerMenuNavigator {

    override fun toRoutineSection() {
        // TODO: добавить маршрут рутины
    }

    override fun toSkinDiarySection() {
        backStackHolder.backStack?.add(DiaryNavRoute.List)
    }

    override fun toMakeupBagSection() {
        backStackHolder.backStack?.add(MakeupBagNavRoute.ProductList)
    }

    override fun toProfileSection() {
        // TODO: добавить маршрут профиля
    }
}