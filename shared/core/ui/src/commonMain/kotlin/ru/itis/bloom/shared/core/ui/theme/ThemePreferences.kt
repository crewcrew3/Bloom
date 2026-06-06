package ru.itis.bloom.shared.core.ui.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemePreferences {
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null) // null = системная
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val current = _isDarkTheme.value
        _isDarkTheme.value = !(current ?: false)
    }
}