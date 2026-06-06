package ru.itis.bloom.shared.feature.auth.impl.domain.usecase

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.auth.api.AuthRepository
import ru.itis.bloom.shared.feature.auth.api.model.request.ConfirmResetPasswordRequest
import ru.itis.bloom.shared.feature.auth.api.model.response.MessageResponse

internal class ConfirmResetPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: ConfirmResetPasswordRequest): Result<MessageResponse> =
        repository.confirmResetPassword(request)
}