package ru.itis.bloom.shared.feature.skindiary.impl.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.core.data.AppDatabase
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryRepository
import ru.itis.bloom.shared.feature.skindiary.api.error.DiaryError
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.SyncStatus
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.toDomain
import ru.itis.bloom.shared.feature.skindiary.impl.utils.DiaryMappers.toNewEntryDto
import java.util.UUID
import kotlin.time.Clock

class SkinDiaryRepositoryImpl(
    private val api: SkinDiaryApi,
    database: AppDatabase,
    private val tokenStorage: TokenStorage,
) : SkinDiaryRepository {

    private val queries = database.appDatabaseQueries

    /**
     * Получает userId из TokenStorage.
     * Бросает исключение, если пользователь не авторизован.
     */
    private suspend fun requireUserId(): String {
        return tokenStorage.getUserId()
            ?: throw IllegalStateException("User not authenticated: userId is null")
    }

    override fun getEntriesFlow(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): Flow<Result<DiaryEntriesPageResponse>> {
        return flow {
            try {
                println("[DiaryRepo] getEntriesFlow: fetching from network")

                val userId = requireUserId()
                println("[DiaryRepo] userId: $userId")

                val networkResponse = api.getEntries(fromDate, toDate, sort, page, size)
                println("[DiaryRepo] Network response: ${networkResponse.content.size} entries")

                // Сохраняем все записи в локальную БД с привязкой к userId
                networkResponse.content.forEach { entry ->
                    queries.upsertEntry(
                        id = entry.id,
                        user_id = userId,
                        entry_date = entry.entryDate,
                        skin_condition = entry.skinCondition.toLong(),
                        hydration_level = entry.hydrationLevel?.toLong(),
                        problem_zones = entry.problemZones,
                        notes = entry.notes,
                        photo_url = entry.photoUrl,
                        sync_status = entry.syncStatus,
                        created_at = entry.createdAt,
                        updated_at = entry.updatedAt
                    )
                }
                println("[DiaryRepo] Saved to local DB")

                emit(Result.success(networkResponse))
            } catch (e: Exception) {
                println("[DiaryRepo] Network error: ${e.message}, falling back to local DB")

                val userId = try {
                    requireUserId()
                } catch (e: IllegalStateException) {
                    emit(Result.failure(DiaryError.NetworkError("User not authenticated")))
                    return@flow
                }

                // Теперь передаём user_id в запрос
                val dbEntries = queries.getEntriesFiltered(
                    user_id = userId,
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
            println("[DiaryRepo] Flow error: ${e.message}")
            emit(Result.failure(DiaryError.NetworkError(e.message ?: "Unknown error")))
        }
    }

    override suspend fun getEntryById(id: String): Result<DiaryEntry> {
        return try {
            val userId = requireUserId()
            val entry = api.getEntryById(id)

            queries.upsertEntry(
                id = entry.id,
                user_id = userId,
                entry_date = entry.entryDate,
                skin_condition = entry.skinCondition.toLong(),
                hydration_level = entry.hydrationLevel?.toLong(),
                problem_zones = entry.problemZones,
                notes = entry.notes,
                photo_url = entry.photoUrl,
                sync_status = entry.syncStatus,
                created_at = entry.createdAt,
                updated_at = entry.updatedAt
            )

            Result.success(entry)
        } catch (e: Exception) {
            println("[DiaryRepo] getEntryById error: ${e.message}, trying local DB")
            val dbEntry = queries.getEntryById(id = id).executeAsOneOrNull()
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
            val userId = requireUserId()

            val savedEntry = if (isUpdate && id != null) {
                api.updateEntry(id, request, photoBytes)
            } else {
                api.createEntry(request, photoBytes)
            }

            queries.upsertEntry(
                id = savedEntry.id,
                user_id = userId,
                entry_date = savedEntry.entryDate,
                skin_condition = savedEntry.skinCondition.toLong(),
                hydration_level = savedEntry.hydrationLevel?.toLong(),
                problem_zones = savedEntry.problemZones,
                notes = savedEntry.notes,
                photo_url = savedEntry.photoUrl,
                sync_status = savedEntry.syncStatus,
                created_at = savedEntry.createdAt,
                updated_at = savedEntry.updatedAt
            )

            Result.success(savedEntry)

        } catch (e: Exception) {
            println("[DiaryRepo] saveEntry network error: ${e.message}, saving locally")

            val userId = try {
                requireUserId()
            } catch (e: IllegalStateException) {
                return Result.failure(DiaryError.NetworkError("User not authenticated"))
            }

            val tempId = id ?: UUID.randomUUID().toString()
            val localEntry = request.toNewEntryDto(userId, tempId)

            queries.upsertEntry(
                id = localEntry.id,
                user_id = userId,
                entry_date = localEntry.entryDate,
                skin_condition = localEntry.skinCondition.toLong(),
                hydration_level = localEntry.hydrationLevel?.toLong(),
                problem_zones = localEntry.problemZones,
                notes = localEntry.notes,
                photo_url = localEntry.photoUrl,
                sync_status = localEntry.syncStatus,
                created_at = localEntry.createdAt,
                updated_at = localEntry.updatedAt
            )

            Result.success(localEntry)
        }
    }

    override suspend fun deleteEntry(id: String): Result<Unit> {
        return try {
            val userId = requireUserId()
            api.deleteEntry(id)
            queries.deleteEntryById(id = id)
            println("[DiaryRepo] Deleted entry $id from network and local DB")
            Result.success(Unit)
        } catch (e: Exception) {
            println("[DiaryRepo] deleteEntry network error: ${e.message}, deleting locally only")
            queries.deleteEntryById(id = id)
            Result.success(Unit)
        }
    }

    override suspend fun syncPendingEntries() {
        val userId = try {
            requireUserId()
        } catch (e: IllegalStateException) {
            println("[DiaryRepo] Cannot sync: user not authenticated")
            return
        }

        // Теперь передаём user_id
        val pending = queries.getPendingEntries(user_id = userId).executeAsList()
        println("[DiaryRepo] Syncing ${pending.size} pending entries for user $userId")

        pending.forEach { dbEntry ->
            try {
                // Исправлены имена параметров
                queries.updateSyncStatus(
                    sync_status = SyncStatus.SYNCED.apiValue,
                    updated_at = Clock.System.now().toString(),
                    id = dbEntry.id
                )
                println("[DiaryRepo] Synced entry ${dbEntry.id}")
            } catch (e: Exception) {
                println("[DiaryRepo] Failed to sync entry ${dbEntry.id}: ${e.message}")
            }
        }
    }
}