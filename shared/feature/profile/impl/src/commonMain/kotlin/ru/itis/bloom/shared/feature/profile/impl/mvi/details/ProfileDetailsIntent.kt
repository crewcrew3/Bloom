package ru.itis.bloom.shared.feature.profile.impl.mvi.details

internal sealed class ProfileDetailsIntent {
    data object LoadProfile : ProfileDetailsIntent()
    data object Logout : ProfileDetailsIntent()
    data object NavigateBack : ProfileDetailsIntent()

    data class OpenDialog(val type: EditDialogType) : ProfileDetailsIntent()
    data object CloseDialog : ProfileDetailsIntent()

    data class UpdateName(val name: String) : ProfileDetailsIntent()
    data class UpdateEmail(val email: String) : ProfileDetailsIntent()
    data class UpdateAvatar(val uri: String) : ProfileDetailsIntent()
    data object DeleteAvatar : ProfileDetailsIntent()

    data class ChangePassword(
        val currentPassword: String,
        val newPassword: String,
        val confirmPassword: String
    ) : ProfileDetailsIntent()
}