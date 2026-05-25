package ru.itis.bloom.shared.feature.skindiary.api.model

enum class SyncStatus(val apiValue: String) {
    SYNCED("synced"),
    PENDING("pending");

    companion object {
        fun fromApi(value: String?): SyncStatus =
            values().find { it.apiValue == value } ?: SYNCED
    }
}