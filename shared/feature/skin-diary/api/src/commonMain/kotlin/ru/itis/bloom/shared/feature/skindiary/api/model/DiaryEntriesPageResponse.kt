package ru.itis.bloom.shared.feature.skindiary.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryEntriesPageResponse(
    @SerialName("content")
    val content: List<DiaryEntry>,
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("total_elements")
    val totalElements: Int,
    @SerialName("total_pages")
    val totalPages: Int
)