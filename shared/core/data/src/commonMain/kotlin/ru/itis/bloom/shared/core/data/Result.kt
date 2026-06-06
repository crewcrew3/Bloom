package ru.itis.bloom.shared.core.data

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

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
    if (BuildKonfig.IS_DEBUG) {
        println("❌ HTTP Error: ${e::class.simpleName} - ${e.message}")
        e.printStackTrace()
    }

    return when (e) {
        // Ktor HTTP ошибки
        is ClientRequestException -> when (e.response.status.value) {
            400 -> CommonError.ValidationError
            401 -> CommonError.Unauthorized
            403 -> CommonError.Forbidden
            404 -> CommonError.NotFound
            409 -> CommonError.Conflict
            422 -> CommonError.ValidationError
            in 500..599 -> CommonError.ServerError
            else -> CommonError.Unknown
        }
        is ServerResponseException -> CommonError.ServerError

        // Таймауты
        is HttpRequestTimeoutException,
        is SocketTimeoutException -> CommonError.Timeout

        // Сеть недоступна
        is UnknownHostException,
        is ConnectException,
        is java.net.SocketException -> CommonError.NetworkUnavailable

        // Отмена запроса
        is CancellationException -> throw e // не маппим, пробрасываем

        // Остальное
        else -> {
            if (BuildKonfig.IS_DEBUG) {
                println("⚠️ Unmapped error: ${e::class.qualifiedName}")
            }
            CommonError.Unknown
        }
    }
}