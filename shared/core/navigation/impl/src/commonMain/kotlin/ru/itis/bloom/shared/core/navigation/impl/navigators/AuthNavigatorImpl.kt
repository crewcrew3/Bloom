package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute

class AuthNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : AuthNavigator {
    override fun toLoginScreen() {
        backStackHolder.backStack?.add(AuthNavRoute.Login)
    }

    override fun toSignUpScreen() {
        backStackHolder.backStack?.add(AuthNavRoute.SignUp)
    }

    override fun back() {
        backStackHolder.backStack?.removeLastOrNull()
    }
}