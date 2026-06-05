package ru.itis.bloom.shared.feature.profile.api

import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.model.ChangePasswordRequestDto
import ru.itis.bloom.shared.core.data.Result as BloomResult

interface ProfileRepository {
    suspend fun getProfile(): BloomResult<UserProfile>
    suspend fun updateProfile(
        name: String?,
        email: String?,
        avatarBytes: ByteArray?,
        deleteAvatar: Boolean
    ): BloomResult<UserProfile>
    suspend fun changePassword(request: ChangePasswordRequestDto): BloomResult<Unit>
}