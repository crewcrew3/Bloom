package ru.itis.bloom.shared.feature.skindiary.api

import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

interface SkinDiaryApi {
    suspend fun getEntries(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): DiaryEntriesPageResponse

    suspend fun getEntryById(id: String): DiaryEntry

    suspend fun createEntry(
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?
    ): DiaryEntry

    suspend fun updateEntry(
        id: String,
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?
    ): DiaryEntry

    suspend fun deleteEntry(id: String)
}