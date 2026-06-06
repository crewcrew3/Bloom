package ru.itis.bloom.shared.feature.profile.impl.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.api.ProfileApi
import ru.itis.bloom.shared.feature.profile.api.error.ProfileError
import ru.itis.bloom.shared.feature.profile.api.model.ChangePasswordRequestDto

internal class ProfileApiImpl(
    private val httpClient: HttpClient
) : ProfileApi {
    private val json = Json { ignoreUnknownKeys = true }
    override suspend fun getProfile(): BloomResult<UserProfile> {
        return try {
            val response = httpClient.get("profile")
            BloomResult.Success(response.body())
        } catch (e: Exception) {
            BloomResult.Error(mapToProfileError(e))
        }
    }

    override suspend fun updateProfile(
        name: String?,
        email: String?,
        avatarBytes: ByteArray?,
        deleteAvatar: Boolean
    ): BloomResult<UserProfile> {
        return try {
            val response = httpClient.request("profile") {
                method = HttpMethod.Put
                expectSuccess = false
                setBody(MultiPartFormDataContent(formData {
                    name?.let { append("name", it) }
                    email?.let { append("email", it) }

                    when {
                        avatarBytes != null -> {
                            append(
                                key = "avatar",
                                value = avatarBytes,
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        ContentType.Image.JPEG.toString()
                                    )
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"avatar.jpg\""
                                    )
                                }
                            )
                        }

                        deleteAvatar -> {
                            append(
                                key = "avatar",
                                value = ByteArray(0),
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        ContentType.Image.JPEG.toString()
                                    )
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"avatar.jpg\""
                                    )
                                }
                            )
                        }
                    }
                }))
            }

            if (!response.status.isSuccess()) {
                val errorCode = extractErrorCode(response)
                return BloomResult.Error(mapErrorCodeToError(errorCode, response.status.value))
            }

            BloomResult.Success(response.body())
        } catch (e: Exception) {
            BloomResult.Error(mapToProfileError(e))
        }
    }

    override suspend fun changePassword(request: ChangePasswordRequestDto): BloomResult<Unit> {
        return try {
            val response = httpClient.request("profile/password") {
                method = HttpMethod.Put
                expectSuccess = false
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (!response.status.isSuccess()) {
                val errorCode = extractErrorCode(response)
                return BloomResult.Error(mapErrorCodeToError(errorCode, response.status.value))
            }

            BloomResult.Success(Unit)
        } catch (e: Exception) {
            BloomResult.Error(mapToProfileError(e))
        }
    }

    private suspend fun extractErrorCode(response: HttpResponse): String? {
        return try {
            val bodyText = response.bodyAsText()
            val jsonObject = json.parseToJsonElement(bodyText) as? JsonObject
            jsonObject?.get("error")?.jsonPrimitive?.contentOrNull
        } catch (e: Exception) {
            null
        }
    }

    private fun mapErrorCodeToError(errorCode: String?, status: Int): BaseError {
        return when (errorCode) {
            "EMAIL_ALREADY_EXISTS" -> ProfileError.EmailAlreadyExists
            "WRONG_CURRENT_PASSWORD" -> ProfileError.WrongCurrentPassword
            "PASSWORD_MISMATCH" -> ProfileError.PasswordMismatch
            else -> when (status) {
                400 -> CommonError.ValidationError
                401 -> CommonError.Unauthorized
                404 -> ProfileError.ProfileNotFound
                409 -> ProfileError.EmailAlreadyExists
                in 500..599 -> CommonError.ServerError
                else -> CommonError.Unknown
            }
        }
    }

    private fun mapToProfileError(e: Exception): BaseError {
        return when (e) {
            is HttpRequestTimeoutException,
            is java.net.SocketTimeoutException -> CommonError.Timeout

            is java.net.UnknownHostException,
            is java.net.ConnectException -> CommonError.NetworkUnavailable

            else -> CommonError.Unknown
        }
    }
}