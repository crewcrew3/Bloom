package ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.diary_error_network
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.feature.skindiary.impl.domain.usecase.GetDiaryEntriesUseCase

internal class DiaryListViewModel(
    private val getDiaryEntriesUseCase: GetDiaryEntriesUseCase,
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

                DiaryListIntent.LoadInitial -> loadEntries(resetPage = true)
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

        getDiaryEntriesUseCase(
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
                    println(response.content.map { println(it.toString()) })
                    _state.update {
                        it.copy(
                            entries = (if (resetPage) response.content else it.entries + response.content).toImmutableList(),
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