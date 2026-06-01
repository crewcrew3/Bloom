package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute

class AuthNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : AuthNavigator {
    override fun toLoginScreen() {
        backStackHolder.backStack?.add(AuthNavRoute.Login)
    }

    override fun toSignUpScreen() {
        backStackHolder.backStack?.add(AuthNavRoute.SignUp)
    }

    override fun toMainScreen() {
        val backStack = backStackHolder.backStack ?: return
        backStack.clear()
        backStack.add(MakeupBagNavRoute.ProductList)
    }

    override fun back() {
        backStackHolder.backStack?.removeLastOrNull()
    }
}