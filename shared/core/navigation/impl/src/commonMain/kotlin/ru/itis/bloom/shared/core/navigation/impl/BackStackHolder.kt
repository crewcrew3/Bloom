package ru.itis.bloom.shared.core.navigation.impl

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class BackStackHolder {
    var backStack: NavBackStack<NavKey>? = null
        private set

    fun setBackStack(backStack: NavBackStack<NavKey>) {
        this.backStack = backStack
    }
}