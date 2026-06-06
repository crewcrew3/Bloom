package ru.itis.bloom.shared.feature.profile.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository

internal class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        name: String? = null,
        email: String? = null,
        avatarBytes: ByteArray? = null,
        deleteAvatar: Boolean = false
    ): BloomResult<UserProfile> = repository.updateProfile(name, email, avatarBytes, deleteAvatar)
}