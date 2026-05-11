package ru.itis.bloom.shared.feature.auth.impl.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.apiCall
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.feature.auth.api.error.AuthError
import ru.itis.bloom.shared.feature.auth.api.model.request.*
import ru.itis.bloom.shared.feature.auth.api.model.response.*

class AuthApiImpl(private val httpClient: HttpClient) : AuthApi {

    override suspend fun register(request: RegisterRequest): Result<MessageResponse> {
        return try {
            val response = httpClient.post("/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            when (response.status.value) {
                409 -> Result.Error(AuthError.EmailAlreadyExists)
                400 -> Result.Error(CommonError.ValidationError)
                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun verifyEmail(token: String): Result<MessageResponse> {
        return try {
            val response = httpClient.get("/auth/verify") {
                parameter("token", token)
            }
            when (response.status.value) {
                400 -> Result.Error(AuthError.TokenInvalidOrExpired)
                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun login(request: LoginRequest): Result<Unit> {
        return apiCall {
            val response = httpClient.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthTokensResponse>()

            // Токены сохраняются ВНУТРИ репозитория
//            tokenStorage.saveTokens(
//                accessToken = response.accessToken,
//                refreshToken = response.refreshToken,
//                expiresIn = response.expiresIn
//            )
        }
    }

    override suspend fun refreshToken(request: RefreshTokenRequest): Result<AuthTokensResponse> {
        return try {
            val response = httpClient.post("/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            when (response.status.value) {
                401 -> Result.Error(AuthError.RefreshTokenInvalid)
                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<MessageResponse> {
        return apiCall {
            httpClient.post("/auth/reset-password") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    override suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): Result<MessageResponse> {
        return try {
            val response = httpClient.post("/auth/reset-password/confirm") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            when (response.status.value) {
                400 -> Result.Error(AuthError.TokenInvalidOrExpired)
                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    private fun mapToAuthError(e: Exception): ru.itis.bloom.shared.core.data.error.BaseError {
        return when (e) {
            is ClientRequestException -> when (e.response.status.value) {
                401 -> AuthError.InvalidCredentials
                403 -> AuthError.EmailNotVerified
                409 -> AuthError.EmailAlreadyExists
                400 -> CommonError.ValidationError
                else -> CommonError.Unknown
            }
            is HttpRequestTimeoutException, is java.net.SocketTimeoutException -> CommonError.Timeout
            is java.net.UnknownHostException, is java.net.ConnectException -> CommonError.NetworkUnavailable
            else -> CommonError.Unknown
        }
    }
}