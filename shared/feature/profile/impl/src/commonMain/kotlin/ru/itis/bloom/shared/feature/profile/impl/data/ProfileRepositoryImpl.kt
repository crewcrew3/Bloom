package ru.itis.bloom.shared.feature.profile.impl.data

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.profile.api.ProfileApi
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.model.ChangePasswordRequestDto

internal class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {
    override suspend fun getProfile(): BloomResult<UserProfile> = api.getProfile()
    override suspend fun updateProfile(
        name: String?,
        email: String?,
        avatarBytes: ByteArray?,
        deleteAvatar: Boolean
    ): BloomResult<UserProfile> = api.updateProfile(name, email, avatarBytes, deleteAvatar)

    override suspend fun changePassword(request: ChangePasswordRequestDto): BloomResult<Unit> =
        api.changePassword(request)
}