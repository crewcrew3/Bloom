package ru.itis.bloom.shared.core.navigation.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.itis.bloom.shared.core.navigation.api.AuthNavigator
import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.api.DiaryNavigator
import ru.itis.bloom.shared.core.navigation.api.BurgerMenuNavigator
import ru.itis.bloom.shared.core.navigation.api.MakeupBagNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.core.navigation.impl.navigators.AuthNavigatorImpl
import ru.itis.bloom.shared.core.navigation.impl.navigators.BottomBarNavigatorImpl
import ru.itis.bloom.shared.core.navigation.impl.navigators.DiaryNavigatorImpl
import ru.itis.bloom.shared.core.navigation.impl.navigators.BurgerMenuNavigatorImpl
import ru.itis.bloom.shared.core.navigation.impl.navigators.MakeupBagNavigatorImpl

val navigationModule = module {
    // Singleton: один BackStackHolder на всё приложение
    single { BackStackHolder() }

    // Bind интерфейсов к реализациям
    singleOf(::BottomBarNavigatorImpl) bind BottomBarNavigator::class
    singleOf(::BurgerMenuNavigatorImpl) bind BurgerMenuNavigator::class
    singleOf(::AuthNavigatorImpl) bind AuthNavigator::class
    singleOf(::MakeupBagNavigatorImpl) bind MakeupBagNavigator::class
    singleOf(::DiaryNavigatorImpl) bind DiaryNavigator::class
}