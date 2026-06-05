package ru.itis.bloom.shared.feature.profile.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile

internal class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}