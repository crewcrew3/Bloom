package ru.itis.bloom.shared.feature.profile.impl.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.profile.api.ProfileApi
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.error.ProfileError

internal class ProfileApiImpl(
    private val httpClient: HttpClient
) : ProfileApi {
    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = httpClient.get("profile")
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToProfileError(e))
        }
    }

    private fun mapToProfileError(e: Exception): BaseError {
        return when (e) {
            is ClientRequestException -> when (e.response.status.value) {
                401 -> CommonError.Unauthorized
                404 -> ProfileError.ProfileNotFound
                else -> CommonError.Unknown
            }
            is HttpRequestTimeoutException -> CommonError.Timeout
            else -> CommonError.NetworkUnavailable
        }
    }
}