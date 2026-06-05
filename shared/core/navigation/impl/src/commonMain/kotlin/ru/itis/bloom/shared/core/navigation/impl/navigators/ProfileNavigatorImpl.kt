package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.ProfileNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute

class ProfileNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : ProfileNavigator {

    override fun toLoginScreen() {
        backStackHolder.backStack?.clear()  // очищаем стек
        backStackHolder.backStack?.add(AuthNavRoute.Login)
    }

    override fun back() {
        backStackHolder.backStack?.removeLastOrNull()
    }
}