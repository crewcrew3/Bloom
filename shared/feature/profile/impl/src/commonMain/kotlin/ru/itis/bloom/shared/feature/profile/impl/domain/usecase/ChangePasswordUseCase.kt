package ru.itis.bloom.shared.feature.profile.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository
import ru.itis.bloom.shared.feature.profile.api.model.ChangePasswordRequestDto

internal class ChangePasswordUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(request: ChangePasswordRequestDto): BloomResult<Unit> =
        repository.changePassword(request)
}