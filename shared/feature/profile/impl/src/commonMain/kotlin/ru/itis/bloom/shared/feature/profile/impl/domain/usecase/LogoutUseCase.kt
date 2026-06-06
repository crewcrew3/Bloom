package ru.itis.bloom.shared.feature.profile.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.response.MessageResponse

internal class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<MessageResponse> =
        authRepository.logout()
}