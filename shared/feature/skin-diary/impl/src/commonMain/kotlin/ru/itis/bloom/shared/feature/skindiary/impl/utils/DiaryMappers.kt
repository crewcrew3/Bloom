package ru.itis.bloom.shared.feature.skindiary.impl.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.itis.bloom.shared.core.data.AppDatabaseQueries
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry
import ru.itis.bloom.shared.feature.skindiary.api.model.SyncStatus
import java.util.UUID
import kotlin.time.Clock
import ru.itis.bloom.shared.core.data.Diary_entries as DbEntry

object DiaryMappers {

    fun DiaryEntry.toDb(userId: String): DbEntry {
        return DbEntry(
            id = this.id,
            user_id = userId,
            entry_date = this.entryDate,
            skin_condition = this.skinCondition.toLong(),
            hydration_level = this.hydrationLevel?.toLong(),
            problem_zones = this.problemZones,
            notes = this.notes,
            photo_url = this.photoUrl,
            sync_status = this.syncStatus,
            created_at = this.createdAt,
            updated_at = this.updatedAt
        )
    }

    fun DbEntry.toDomain(): DiaryEntry {
        return DiaryEntry(
            id = this.id,
            userId = this.user_id,
            entryDate = this.entry_date,
            skinCondition = this.skin_condition.toInt(),
            hydrationLevel = this.hydration_level?.toInt(),
            problemZones = this.problem_zones,
            notes = this.notes,
            photoUrl = this.photo_url,
            syncStatus = this.sync_status,
            createdAt = this.created_at,
            updatedAt = this.updated_at
        )
    }

    fun CreateDiaryEntryRequest.toNewEntryDto(
        userId: String,
        id: String = UUID.randomUUID().toString()
    ): DiaryEntry {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return DiaryEntry(
            id = id,
            userId = userId,
            entryDate = this.entryDate,
            skinCondition = this.skinCondition,
            hydrationLevel = this.hydrationLevel,
            problemZones = this.problemZones,
            notes = this.notes,
            photoUrl = null,
            syncStatus = SyncStatus.PENDING.apiValue,
            createdAt = now,
            updatedAt = now
        )
    }
    fun AppDatabaseQueries.upsertEntry(entry: DbEntry) {
        upsertEntry(
            id = entry.id,
            user_id = entry.user_id,
            entry_date = entry.entry_date,
            skin_condition = entry.skin_condition,
            hydration_level = entry.hydration_level,
            problem_zones = entry.problem_zones,
            notes = entry.notes,
            photo_url = entry.photo_url,
            sync_status = entry.sync_status,
            created_at = entry.created_at,
            updated_at = entry.updated_at
        )
    }
}