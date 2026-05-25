package ru.itis.bloom.shared.feature.skindiary.impl.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_error_network
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository

class DiaryListViewModel(
    private val repository: SkinDiaryRepository,
    //private val analytics: AnalyticsTracker
) : ViewModel() {

    private val _state = MutableStateFlow(DiaryListState())
    val state: StateFlow<DiaryListState> = _state.asStateFlow()

    private val _effects = Channel<DiaryListEffect>(Channel.BUFFERED)
    val effects: Flow<DiaryListEffect> = _effects.receiveAsFlow()

    init {
        //analytics.logScreenOpen("diary_list")
        onIntent(DiaryListIntent.LoadInitial)
    }

    fun onIntent(intent: DiaryListIntent) {
        viewModelScope.launch {
            when (intent) {
                is DiaryListIntent.ChangeDateRange -> {
                    _state.update {
                        it.copy(
                            dateRange = intent.from to intent.to,
                            currentPage = 0,
                            hasMore = true
                        )
                    }
                    loadEntries(resetPage = true)
                }

                is DiaryListIntent.ChangeSort -> {
                    _state.update { it.copy(sort = intent.order, currentPage = 0, hasMore = true) }
                    loadEntries(resetPage = true)
                }

                DiaryListIntent.Refresh -> loadEntries(resetPage = true, isRefresh = true)
                DiaryListIntent.LoadNextPage -> loadEntries(resetPage = false)
                is DiaryListIntent.NavigateToDetail -> _effects.send(
                    DiaryListEffect.NavigateToDetail(
                        intent.entryId
                    )
                )

                DiaryListIntent.LoadInitial -> Unit
                else -> {}
            }
        }
    }

    private suspend fun loadEntries(resetPage: Boolean, isRefresh: Boolean = false) {
        val currentState = _state.value
        if (!resetPage && (!currentState.hasMore || currentState.isLoading)) return

        val page = if (resetPage) 0 else currentState.currentPage + 1
        _state.update {
            it.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                currentPage = page,
                error = null
            )
        }

        repository.getEntriesFlow(
            fromDate = currentState.dateRange.first,
            toDate = currentState.dateRange.second,
            sort = currentState.sort.apiValue,
            page = page,
            size = 20
        ).catch { e ->
            _effects.send(DiaryListEffect.ShowError(Res.string.diary_error_network))
            _state.update { it.copy(isLoading = false, isRefreshing = false, error = e.message) }
        }.collect { result ->
            result.fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            entries = if (resetPage) response.content else it.entries + response.content,
                            isLoading = false,
                            isRefreshing = false,
                            currentPage = page,
                            hasMore = response.page < response.totalPages - 1
                        )
                    }
                },
                onFailure = { e ->
                    _effects.send(DiaryListEffect.ShowError(Res.string.diary_error_network))
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }
}