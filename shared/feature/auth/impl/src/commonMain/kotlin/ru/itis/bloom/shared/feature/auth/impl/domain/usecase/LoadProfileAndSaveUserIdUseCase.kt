package ru.itis.bloom.shared.feature.auth.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.ProfileRepository

internal class LoadProfileAndSaveUserIdUseCase(
    private val profileRepository: ProfileRepository,
    private val tokenStorage: TokenStorage
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return when (val result = profileRepository.getProfile()) {
            is Result.Success -> {
                // Сохраняем user_id для использования в других фичах (дневник, косметичка и т.д.)
                tokenStorage.saveUserId(result.data.id)
                result
            }

            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}