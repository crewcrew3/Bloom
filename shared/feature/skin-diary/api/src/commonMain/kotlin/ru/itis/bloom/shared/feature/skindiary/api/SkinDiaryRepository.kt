package ru.itis.bloom.shared.feature.skindiary.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

interface SkinDiaryRepository {
    fun getEntriesFlow(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): Flow<Result<DiaryEntriesPageResponse>>

    suspend fun getEntryById(id: String): Result<DiaryEntry>

    suspend fun saveEntry(
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?,
        isUpdate: Boolean = false,
        id: String? = null
    ): Result<DiaryEntry>

    suspend fun deleteEntry(id: String): Result<Unit>

    suspend fun syncPendingEntries()
}