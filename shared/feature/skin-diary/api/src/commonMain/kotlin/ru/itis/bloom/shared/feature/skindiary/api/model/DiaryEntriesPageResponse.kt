package ru.itis.bloom.shared.feature.skindiary.api.model

import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntriesPageResponse(
    val content: List<DiaryEntry>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)