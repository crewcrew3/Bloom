package ru.itis.bloom.shared.feature.skindiary.impl.domain

sealed interface DiaryEvent {
    data object EntryCreated : DiaryEvent
    data object EntryUpdated : DiaryEvent
    data class EntryDeleted(val entryId: String) : DiaryEvent
}