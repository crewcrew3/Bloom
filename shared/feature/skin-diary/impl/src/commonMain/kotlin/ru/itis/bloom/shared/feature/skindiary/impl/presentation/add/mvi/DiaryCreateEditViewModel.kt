package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.error_saving_entry
import bloom.shared.feature.skin_diary.impl.generated.resources.error_saving_photo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.core.domain.usecase.ImageUriToByteArrayUseCase
import ru.itis.bloom.shared.core.ui.analytics.AnalyticsHelper
import ru.itis.bloom.shared.core.ui.analytics.ScreenName
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import ru.itis.bloom.shared.feature.skindiary.impl.domain.model.SaveDiaryEntryCommand
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntryByIdUseCase
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.SaveDiaryEntryUseCase

internal class DiaryCreateEditViewModel(
    private val saveEntryUseCase: SaveDiaryEntryUseCase,
    private val getEntryByIdUseCase: GetDiaryEntryByIdUseCase,
    private val imageUriToByteArrayUseCase: ImageUriToByteArrayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DiaryCreateEditState())
    val state: StateFlow<DiaryCreateEditState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<DiaryCreateEditEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<DiaryCreateEditEffect> = _effects.asSharedFlow()

    init {
        AnalyticsHelper.logScreenOpen(ScreenName.SKIN_DIARY_CREATE_EDIT)
    }

    fun init(entryId: String? = null) {
        if (entryId != null) {
            loadEntry(entryId)
        }
    }

    private fun loadEntry(entryId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getEntryByIdUseCase(entryId)
                .onSuccess { entry ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            entryId = entry.id,
                            date = LocalDate.parse(entry.entryDate),
                            skinCondition = entry.skinCondition,
                            hydrationLevel = entry.hydrationLevel ?: 3,
                            problemZones = ProblemZone.fromJson(entry.problemZones),
                            notes = entry.notes ?: "",
                            photoUrl = entry.photoUrl
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
                        DiaryCreateEditEffect.ShowError(Res.string.error_saving_entry)
                    )
                }
        }
    }

    fun onIntent(intent: DiaryCreateEditIntent) {
        when (intent) {
            is DiaryCreateEditIntent.SetDate -> {
                _state.update { it.copy(date = intent.date) }
            }

            is DiaryCreateEditIntent.SetSkinCondition -> {
                _state.update { it.copy(skinCondition = intent.value) }
            }

            is DiaryCreateEditIntent.SetHydrationLevel -> {
                _state.update { it.copy(hydrationLevel = intent.value) }
            }

            is DiaryCreateEditIntent.ToggleProblemZone -> {
                _state.update { currentState ->
                    val currentZones = currentState.problemZones.toMutableList()
                    if (currentZones.contains(intent.zone)) {
                        currentZones.remove(intent.zone)
                    } else {
                        currentZones.add(intent.zone)
                    }
                    currentState.copy(problemZones = currentZones)
                }
            }

            is DiaryCreateEditIntent.SetNotes -> {
                _state.update { it.copy(notes = intent.notes) }
            }

            is DiaryCreateEditIntent.RequestPhotoSelection -> processPhotoUri(intent.uri)
            is DiaryCreateEditIntent.PhotoProcessingStarted -> _state.update {
                it.copy(
                    isPhotoProcessing = true,
                    photoError = null
                )
            }

            is DiaryCreateEditIntent.PhotoProcessingFinished -> _state.update {
                it.copy(
                    isPhotoProcessing = false,
                    photoBytes = intent.photoBytes,
                    photoUrl = null,
                    photoError = null
                )
            }

            is DiaryCreateEditIntent.PhotoProcessingError -> _state.update {
                it.copy(isPhotoProcessing = false, photoError = intent.message)
            }

            is DiaryCreateEditIntent.RemovePhoto -> {
                _state.update { it.copy(photoBytes = null, photoUrl = null) }
            }

            is DiaryCreateEditIntent.SaveEntry -> {
                saveEntry()
            }

            is DiaryCreateEditIntent.NavigateBack -> {
                viewModelScope.launch {
                    _effects.emit(DiaryCreateEditEffect.NavigateBack)
                }
            }
        }
    }

    private fun processPhotoUri(uri: String) {
        viewModelScope.launch {
            _state.update { it.copy(isPhotoProcessing = true, photoError = null) }

            imageUriToByteArrayUseCase.execute(uri)
                .onSuccess { bytes ->
                    _state.update {
                        it.copy(
                            isPhotoProcessing = false,
                            photoBytes = bytes,
                            photoUrl = null,
                            photoError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isPhotoProcessing = false, photoError = error.message) }
                    _effects.emit(
                        DiaryCreateEditEffect.ShowPhotoError(
                            Res.string.error_saving_photo
                        )
                    )
                }
        }
    }

    private fun saveEntry() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.isPhotoProcessing) return@launch
            _state.update { it.copy(isLoading = true) }

            saveEntryUseCase(
                SaveDiaryEntryCommand(
                    id = currentState.entryId,
                    date = currentState.date,
                    skinCondition = currentState.skinCondition,
                    hydrationLevel = currentState.hydrationLevel,
                    problemZones = currentState.problemZones,
                    notes = currentState.notes,
                    photoBytes = currentState.photoBytes
                )
            )
                .onSuccess { entry ->
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(DiaryCreateEditEffect.NavigateBack)
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
                        DiaryCreateEditEffect.ShowError(Res.string.error_saving_entry)
                    )
                }
        }
    }
}
