package ru.itis.bloom.shared.feature.profile.api

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile

interface ProfileApi {
    suspend fun getProfile(): BloomResult<UserProfile>
}