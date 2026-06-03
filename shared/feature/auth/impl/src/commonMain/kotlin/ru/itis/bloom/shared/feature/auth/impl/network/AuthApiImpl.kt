package ru.itis.bloom.shared.feature.auth.impl.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.apiCall
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.core.data.model.AuthTokensResponse
import ru.itis.bloom.shared.core.data.model.RefreshTokenRequest
import ru.itis.bloom.shared.core.data.network.token.TokenRefresher
import ru.itis.bloom.shared.core.data.network.token.TokenStorage
import ru.itis.bloom.shared.feature.auth.api.AuthApi
import ru.itis.bloom.shared.feature.auth.api.error.AuthError
import ru.itis.bloom.shared.feature.auth.api.model.request.*
import ru.itis.bloom.shared.feature.auth.api.model.response.*

private const val TAG = "BLOOM_AUTH_API"

internal class AuthApiImpl(
    private val httpClient: HttpClient,
    private val tokenStorage: TokenStorage
) : AuthApi, TokenRefresher {

    override suspend fun register(request: RegisterRequest): Result<MessageResponse> {
        println("[$TAG] register() → email: ${request.email}, name: ${request.name}")
        return try {
            println("[$TAG] → POST /auth/register")
            val response = httpClient.post("auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            println(
                "[$TAG] ← Status: ${response.status.value}, Body: ${
                    response.bodyAsText().take(200)
                }"
            )
            when (response.status.value) {
                409 -> {
                    println("[$TAG] ✗ Email already exists")
                    Result.Error(AuthError.EmailAlreadyExists)
                }

                400 -> {
                    println("[$TAG] ✗ Validation error")
                    Result.Error(CommonError.ValidationError)
                }

                else -> {
                    println("[$TAG] ✓ Success")
                    Result.Success(response.body())
                }
            }
        } catch (e: Exception) {
            println("[$TAG] ✗ Exception: ${e::class.simpleName} - ${e.message}")
            e.printStackTrace()
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun verifyEmail(token: String): Result<MessageResponse> {
        return try {
            val response = httpClient.get("auth/verify") {
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
        println("[$TAG] login() → email: ${request.email}")
        return try {
            println("[$TAG] → POST /auth/login")
            val response = httpClient.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            println("[$TAG] ← Status: ${response.status.value}")
            when (response.status.value) {
                200 -> {
                    val tokens = response.body<AuthTokensResponse>()
                    tokenStorage.saveTokens(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken,
                        expiresIn = tokens.expiresIn ?: 3600
                    )
                    println("[$TAG] ✓ Success, access_token: ${tokens.accessToken.take(20)}...")
                    Result.Success(Unit)
                }

                401 -> {
                    println("[$TAG] ✗ 401 Invalid credentials")
                    Result.Error(AuthError.InvalidCredentials)
                }

                403 -> {
                    println("[$TAG] ✗ 403 Email not verified")
                    Result.Error(AuthError.EmailNotVerified)
                }

                400 -> {
                    println("[$TAG] ✗ 400 Validation error")
                    Result.Error(CommonError.ValidationError)
                }

                else -> {
                    val body = response.bodyAsText()
                    println("[$TAG] ✗ Unexpected status ${response.status.value}, body: $body")
                    Result.Error(CommonError.Unknown)
                }
            }
        } catch (e: Exception) {
            println("[$TAG] ✗ Exception: ${e::class.simpleName} - ${e.message}")
            e.printStackTrace()
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun refreshToken(request: RefreshTokenRequest): Result<AuthTokensResponse> {
        return try {
            val response = httpClient.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            when (response.status.value) {
                200 -> {
                    val tokens = response.body<AuthTokensResponse>()
                    tokenStorage.saveTokens(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken,
                        expiresIn = tokens.expiresIn ?: 3600
                    )
                    println("[$TAG] ✓ Tokens refreshed")
                    Result.Success(tokens)
                }

                401 -> {
                    println("[$TAG] ✗ 401 Refresh token invalid")
                    tokenStorage.clearTokens()
                    Result.Error(AuthError.RefreshTokenInvalid)
                }

                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun refreshTokens(request: RefreshTokenRequest): Result<AuthTokensResponse> {
        return try {
            val response = httpClient.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            when (response.status.value) {
                200 -> {
                    val tokens = response.body<AuthTokensResponse>()
                    // Сохраняем новые токены
                    tokenStorage.saveTokens(
                        accessToken = tokens.accessToken,
                        refreshToken = tokens.refreshToken,
                        expiresIn = tokens.expiresIn ?: 3600
                    )
                    Result.Success(tokens)
                }
                401 -> {
                    tokenStorage.clearTokens()
                    Result.Error(AuthError.RefreshTokenInvalid)
                }
                else -> Result.Error(CommonError.Unknown)
            }
        } catch (e: Exception) {
            Result.Error(mapToAuthError(e))
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<MessageResponse> {
        return apiCall {
            httpClient.post("auth/reset-password") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    override suspend fun confirmResetPassword(request: ConfirmResetPasswordRequest): Result<MessageResponse> {
        return try {
            val response = httpClient.post("auth/reset-password/confirm") {
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

    private fun mapToAuthError(e: Exception): BaseError {
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
