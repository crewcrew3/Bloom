package ru.itis.bloom.shared.feature.profile.impl.mvi.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.domain.error.ImageTooLargeException
import ru.itis.bloom.shared.core.domain.error.UnsupportedImageFormatException
import ru.itis.bloom.shared.core.domain.usecase.ImageUriToByteArrayUseCase
import ru.itis.bloom.shared.core.ui.analytics.AnalyticsHelper
import ru.itis.bloom.shared.core.ui.analytics.ScreenName
import ru.itis.bloom.shared.feature.profile.api.model.ChangePasswordRequestDto
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.ChangePasswordUseCase
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.GetProfileUseCase
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.LogoutUseCase
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.UpdateProfileUseCase
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileErrorMapper
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

internal class ProfileDetailsViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val imageUriToByteArrayUseCase: ImageUriToByteArrayUseCase
) : ViewModel() {

    init {
        AnalyticsHelper.logScreenOpen(ScreenName.PROFILE_DETAILS)
    }

    private val _state = MutableStateFlow(ProfileDetailsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileDetailsEffect>(extraBufferCapacity = 10)
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: ProfileDetailsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProfileDetailsIntent.LoadProfile -> loadProfile()
                is ProfileDetailsIntent.Logout -> logout()
                is ProfileDetailsIntent.NavigateBack -> {
                    _effect.emit(ProfileDetailsEffect.NavigateBack)
                }
                is ProfileDetailsIntent.OpenDialog -> openDialog(intent.type)
                is ProfileDetailsIntent.CloseDialog -> closeDialog()
                is ProfileDetailsIntent.UpdateName -> updateName(intent.name)
                is ProfileDetailsIntent.UpdateEmail -> updateEmail(intent.email)
                is ProfileDetailsIntent.UpdateAvatar -> updateAvatar(intent.uri)
                is ProfileDetailsIntent.DeleteAvatar -> deleteAvatar()
                is ProfileDetailsIntent.ChangePassword -> changePassword(
                    intent.currentPassword,
                    intent.newPassword,
                    intent.confirmPassword
                )
            }
        }
    }

    private suspend fun loadProfile() {
        _state.update { it.copy(isLoading = true) }
        when (val result = getProfileUseCase()) {
            is BloomResult.Success -> _state.update {
                it.copy(userProfile = result.data, isLoading = false)
            }
            is BloomResult.Error -> handleError(result.error)
            is BloomResult.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun logout() {
        _state.update { it.copy(isLoading = true) }
        when (val result = logoutUseCase()) {
            is BloomResult.Success -> {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Success.LoggedOut))
                _effect.emit(ProfileDetailsEffect.NavigateToLogin)
            }
            is BloomResult.Error -> handleError(result.error)
            is BloomResult.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private fun openDialog(type: EditDialogType) {
        _state.update { it.copy(activeDialog = type, dialogError = null) }
    }

    private fun closeDialog() {
        _state.update { it.copy(activeDialog = null, dialogError = null) }
    }

    private suspend fun updateName(name: String) {
        if (name.isBlank()) {
            _state.update { it.copy(dialogError = ProfileMessageRes.Error.EmptyName) }
            return
        }
        performUpdate(name = name)
    }

    private suspend fun updateEmail(email: String) {
        if (email.isBlank()) {
            _state.update { it.copy(dialogError = ProfileMessageRes.Error.EmptyEmail) }
            return
        }
        performUpdate(email = email)
    }

    private suspend fun updateAvatar(uri: String) {
        _state.update { it.copy(isLoading = true) }
        val bytesResult = imageUriToByteArrayUseCase.execute(uri, 5 * 1024 * 1024)
        bytesResult.fold(
            onSuccess = { bytes ->
                performUpdate(avatarBytes = bytes)
            },
            onFailure = { error ->
                _state.update { it.copy(isLoading = false) }
                when (error) {
                    is ImageTooLargeException -> {
                        _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Error.ImageTooLarge))
                    }
                    is UnsupportedImageFormatException -> {
                        _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Error.UnsupportedImageFormat))
                    }
                    else -> {
                        _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Error.Unknown))
                    }
                }
            }
        )
    }

    private suspend fun deleteAvatar() {
        performUpdate(deleteAvatar = true)
    }

    private suspend fun performUpdate(
        name: String? = null,
        email: String? = null,
        avatarBytes: ByteArray? = null,
        deleteAvatar: Boolean = false
    ) {
        _state.update { it.copy(isLoading = true) }
        when (val result = updateProfileUseCase(name, email, avatarBytes, deleteAvatar)) {
            is BloomResult.Success -> {
                _state.update {
                    it.copy(userProfile = result.data, isLoading = false, activeDialog = null)
                }
                _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Success.ProfileUpdated))
            }
            is BloomResult.Error -> handleError(result.error)
            is BloomResult.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        if (newPassword != confirmPassword) {
            _state.update { it.copy(dialogError = ProfileMessageRes.Error.PasswordMismatch) }
            return
        }
        if (newPassword.length < 8) {
            _state.update { it.copy(dialogError = ProfileMessageRes.Error.PasswordTooShort) }
            return
        }

        _state.update { it.copy(isLoading = true) }
        val request = ChangePasswordRequestDto(currentPassword, newPassword, confirmPassword)
        when (val result = changePasswordUseCase(request)) {
            is BloomResult.Success -> {
                _state.update { it.copy(isLoading = false, activeDialog = null) }
                _effect.emit(ProfileDetailsEffect.ShowMessage(ProfileMessageRes.Success.PasswordChanged))
            }
            is BloomResult.Error -> handleError(result.error)
            is BloomResult.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = ProfileErrorMapper.mapToMessageRes(error)
        _state.update { it.copy(isLoading = false) }
        _effect.emit(ProfileDetailsEffect.ShowMessage(messageRes))
    }
}