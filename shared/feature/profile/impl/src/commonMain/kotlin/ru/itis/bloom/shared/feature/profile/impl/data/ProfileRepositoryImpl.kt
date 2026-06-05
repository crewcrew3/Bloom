package ru.itis.bloom.shared.feature.profile.impl.data

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.profile.api.ProfileApi
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile

internal class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> = api.getProfile()
}