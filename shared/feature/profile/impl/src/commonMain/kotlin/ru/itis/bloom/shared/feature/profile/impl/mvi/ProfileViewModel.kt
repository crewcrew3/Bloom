package ru.itis.bloom.shared.feature.profile.impl.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.ui.analytics.AnalyticsHelper
import ru.itis.bloom.shared.core.ui.analytics.ScreenName
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.GetProfileUseCase
import ru.itis.bloom.shared.feature.profile.impl.domain.usecase.LogoutUseCase
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileErrorMapper
import ru.itis.bloom.shared.feature.profile.impl.utils.ProfileMessageRes

internal class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    init {
        AnalyticsHelper.logScreenOpen(ScreenName.PROFILE)
    }

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>(extraBufferCapacity = 10)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: ProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProfileIntent.LoadProfile -> loadProfile()
                is ProfileIntent.Logout -> logout()
                is ProfileIntent.NavigateToProfileDetails -> {
                    _effect.emit(ProfileEffect.NavigateToProfileDetails)
                }
            }
        }
    }

    private suspend fun loadProfile() {
        _state.update { it.copy(isLoading = true) }
        when (val result = getProfileUseCase()) {
            is Result.Success -> _state.update { it.copy(userProfile = result.data, isLoading = false) }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun logout() {
        _state.update { it.copy(isLoading = true) }
        when (val result = logoutUseCase()) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProfileEffect.ShowMessage(ProfileMessageRes.Success.LoggedOut))
                _effect.emit(ProfileEffect.NavigateToLogin)
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = ProfileErrorMapper.mapToMessageRes(error)
        _state.update { it.copy(isLoading = false) }
        _effect.emit(ProfileEffect.ShowMessage(messageRes))
    }
}