package ru.itis.bloom.shared.core.navigation.api

interface AuthNavigator {
    fun toLoginScreen()
    fun toSignUpScreen()
    fun back()
}