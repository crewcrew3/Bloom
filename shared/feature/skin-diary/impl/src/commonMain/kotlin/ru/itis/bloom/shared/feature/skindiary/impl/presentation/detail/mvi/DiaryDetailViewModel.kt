package ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.error_loading_entry
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.ui.analytics.AnalyticsHelper
import ru.itis.bloom.shared.core.ui.analytics.ScreenName
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntryByIdUseCase

internal class DiaryDetailViewModel(
    private val entryId: String,
    private val getDiaryEntryByIdUseCase: GetDiaryEntryByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DiaryDetailState())
    val state: StateFlow<DiaryDetailState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<DiaryDetailEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<DiaryDetailEffect> = _effects.asSharedFlow()

    init {
        AnalyticsHelper.logScreenOpen(ScreenName.SKIN_DIARY_DETAIL)
        loadEntry()
    }

    fun onIntent(intent: DiaryDetailIntent) {
        when (intent) {
            is DiaryDetailIntent.LoadEntry -> loadEntry()
            is DiaryDetailIntent.Reload -> loadEntry()
            is DiaryDetailIntent.EditEntry -> editEntry()
            is DiaryDetailIntent.NavigateBack -> navigateBack()
            is DiaryDetailIntent.DismissError -> dismissError()
        }
    }

    private fun loadEntry() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }

            getDiaryEntryByIdUseCase(entryId)
                .onSuccess { entry ->
                    val problemZones = ProblemZone.fromJson(entry.problemZones)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            entry = entry,
                            problemZonesList = problemZones.toImmutableList()
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = error.message
                        )
                    }
                    _effects.emit(
                        DiaryDetailEffect.ShowError(
                            Res.string.error_loading_entry
                        )
                    )
                }
        }
    }

    private fun editEntry() {
        viewModelScope.launch {
            _effects.emit(DiaryDetailEffect.NavigateToEdit(entryId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effects.emit(DiaryDetailEffect.NavigateBack)
        }
    }

    private fun dismissError() {
        _state.update { it.copy(isError = false, errorMessage = null) }
    }
}