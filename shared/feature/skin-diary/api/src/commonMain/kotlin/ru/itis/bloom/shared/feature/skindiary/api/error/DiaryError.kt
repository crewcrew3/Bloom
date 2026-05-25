package ru.itis.bloom.shared.feature.skindiary.api.error

sealed class DiaryError(message: String) : Exception(message) {
    data class NetworkError(val errorMessage: String) : DiaryError(errorMessage)
    data class ServerError(val code: String, val errorMessage: String) : DiaryError(errorMessage)
    data class NotFound(val id: String) : DiaryError("Not found: $id")
    data class Conflict(val errorMessage: String) : DiaryError(errorMessage)
    class ValidationFailed : DiaryError("Validation failed")
    class Unauthorized : DiaryError("Unauthorized")
}