@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ru.itis.bloom.shared.core.ui.analytics

// На Desktop Firebase не поддерживается
actual object AnalyticsHelper {
    actual fun logScreenOpen(screenName: String) {
        println("[Analytics] $screenName")
    }
}