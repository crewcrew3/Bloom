package ru.itis.bloom.shared.feature.profile.api

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.core.data.Result as BloomResult

interface ProfileRepository {
    suspend fun getProfile(): BloomResult<UserProfile>
}