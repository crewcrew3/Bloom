package ru.itis.bloom.shared.feature.skindiary.impl.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.error.DiaryError
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.SyncStatus
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.toDb
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.toDomain
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.toNewEntryDto
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.upsertEntry


class SkinDiaryRepositoryImpl(
    private val api: SkinDiaryApi,
    database: AppDatabase,
    private val currentUserIdProvider: () -> String
) : SkinDiaryRepository {

    private val queries = database.appDatabaseQueries

    override fun getEntriesFlow(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): Flow<Result<DiaryEntriesPageResponse>> {
        return flow {
            try {
                println("getEntriesFlow")
                val networkResponse = api.getEntries(fromDate, toDate, sort, page, size)

                networkResponse.content.forEach { entry ->
                    println("entry")
                    queries.upsertEntry(entry.toDb(currentUserIdProvider()))
                }
                println("success networkResponse")
                emit(Result.success(networkResponse))
            } catch (e: Exception) {
                println(e.message)
                val dbEntries = queries.getEntriesFiltered(
                    from_date = fromDate?.toString(),
                    to_date = toDate?.toString(),
                    limit = size.toLong(),
                    offset = (page * size).toLong()
                ).executeAsList().map { it.toDomain() }

                val dbPage = DiaryEntriesPageResponse(
                    content = dbEntries,
                    page = page,
                    size = size,
                    totalElements = dbEntries.size,
                    totalPages = 1
                )

                emit(Result.success(dbPage))
            }
        }.catch { e ->
            println(e.message)
            emit(Result.failure(DiaryError.NetworkError(e.message ?: "Unknown error")))
        }
    }

    override suspend fun getEntryById(id: String): Result<DiaryEntry> {
        return try {
            val entry = api.getEntryById(id)
            queries.upsertEntry(entry.toDb(currentUserIdProvider()))
            Result.success(entry)
        } catch (e: Exception) {
            val dbEntry = queries.getEntryById(id).executeAsOneOrNull()
            if (dbEntry != null) Result.success(dbEntry.toDomain())
            else Result.failure(DiaryError.NotFound(id))
        }
    }

    override suspend fun saveEntry(
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?,
        isUpdate: Boolean,
        id: String?
    ): Result<DiaryEntry> {
        return try {
            val newEntryDto = if (isUpdate && id != null) {
                request
            } else {
                request
            }

            val savedEntry = if (isUpdate && id != null) {
                api.updateEntry(id, newEntryDto, photoBytes)
            } else {
                api.createEntry(newEntryDto, photoBytes)
            }

            queries.upsertEntry(savedEntry.toDb(currentUserIdProvider()))
            Result.success(savedEntry)

        } catch (e: Exception) {
            val tempId = id ?: java.util.UUID.randomUUID().toString()
            val localEntry = request.toNewEntryDto(currentUserIdProvider(), tempId)

            queries.upsertEntry(localEntry.toDb(currentUserIdProvider()))

            Result.success(localEntry)
        }
    }

    override suspend fun deleteEntry(id: String): Result<Unit> {
        return try {
            api.deleteEntry(id)
            queries.deleteEntryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            queries.deleteEntryById(id)
            Result.success(Unit)
        }
    }

    override suspend fun syncPendingEntries() {
        val pending = queries.getPendingEntries().executeAsList()
        pending.forEach { dbEntry ->
            try {
                queries.updateSyncStatus(SyncStatus.SYNCED.apiValue, dbEntry.updated_at, dbEntry.id)
            } catch (e: Exception) {
            }
        }
    }
}