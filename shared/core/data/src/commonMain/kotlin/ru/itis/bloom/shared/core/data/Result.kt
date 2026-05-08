package ru.itis.bloom.shared.core.data

import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val error: BaseError) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * Утилита для обёртки сетевых вызовов.
 * Ловит исключения Ktor/сети и маппит в CommonError.
 */
suspend fun <T> apiCall(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(mapToCommonError(e))
    }
}

private fun mapToCommonError(e: Exception): CommonError {
    return when (e) {
        //потом тут будут koin ошибки
        else -> CommonError.Unknown
    }
}