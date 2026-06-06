@file:OptIn(ExperimentalSerializationApi::class)

package ru.itis.bloom.shared.core.navigation.impl

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import ru.itis.bloom.shared.feature.auth.api.navigation.AuthNavRoute
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.profile.api.navigation.ProfileNavRoute
import ru.itis.bloom.shared.feature.skindiary.api.navigation.DiaryNavRoute

val navigationSavedStateConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            // Автоматически регистрирует все @Serializable подтипы
            subclassesOfSealed<AuthNavRoute>()
            subclassesOfSealed<MakeupBagNavRoute>()
            subclassesOfSealed<DiaryNavRoute>()
            subclassesOfSealed<ProfileNavRoute>()
        }
    }
}