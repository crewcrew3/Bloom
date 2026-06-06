package ru.itis.bloom.shared.feature.skindiary.impl.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DiaryEventBus {
    private val _events = MutableSharedFlow<DiaryEvent>(extraBufferCapacity = 10)
    val events: SharedFlow<DiaryEvent> = _events.asSharedFlow()

    suspend fun emit(event: DiaryEvent) {
        _events.emit(event)
    }
}