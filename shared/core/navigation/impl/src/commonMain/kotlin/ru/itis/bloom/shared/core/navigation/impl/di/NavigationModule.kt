package ru.itis.bloom.shared.core.navigation.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.core.navigation.impl.navigators.AuthNavigatorImpl
import ru.itis.bloom.shared.core.navigation.impl.navigators.BottomBarNavigatorImpl

val navigationModule = module {
    // Singleton: один BackStackHolder на всё приложение
    single { BackStackHolder() }

    // Bind интерфейсов к реализациям
    singleOf(::BottomBarNavigatorImpl) bind BottomBarNavigator::class
    singleOf(::AuthNavigatorImpl) bind AuthNavigator::class
}