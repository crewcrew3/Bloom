package ru.itis.bloom.shared.feature.skindiary.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface DiaryNavRoute: NavKey  {
    @Serializable
    data object List : DiaryNavRoute

    @Serializable
    data class Detail(val entryId: String) : DiaryNavRoute

    @Serializable
    data object Create : DiaryNavRoute

    @Serializable
    data class Edit(val entryId: String) : DiaryNavRoute
}