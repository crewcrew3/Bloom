@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ru.itis.bloom.shared.core.ui.analytics

/*
    Подумала, что создавать отдельный модуль для этого избыточно, поэтому пока положила в ui.
    Т.к. аналитика экранов это больше привязка к presentation части, чем к бизнес-логике (?)
 */
expect object AnalyticsHelper {
    fun logScreenOpen(screenName: String)
}