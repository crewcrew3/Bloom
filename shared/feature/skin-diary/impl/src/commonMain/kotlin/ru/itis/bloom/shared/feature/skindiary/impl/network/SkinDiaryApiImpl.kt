package ru.itis.bloom.shared.feature.skindiary.impl.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.datetime.LocalDate
import ru.itis.bloom.shared.feature.skindiary.api.SkinDiaryApi
import ru.itis.bloom.shared.feature.skindiary.api.model.CreateDiaryEntryRequest
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntriesPageResponse
import ru.itis.bloom.shared.feature.skindiary.api.model.DiaryEntry

class SkinDiaryApiImpl(
    private val httpClient: HttpClient
) : SkinDiaryApi {

    override suspend fun getEntries(
        fromDate: LocalDate?,
        toDate: LocalDate?,
        sort: String,
        page: Int,
        size: Int
    ): DiaryEntriesPageResponse {
        return httpClient.get("diary/entries") {
            parameter("from_date", fromDate?.toString())
            parameter("to_date", toDate?.toString())
            parameter("sort", sort)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    override suspend fun getEntryById(id: String): DiaryEntry {
        return httpClient.get("diary/entries/$id").body()
    }

    override suspend fun createEntry(
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?
    ): DiaryEntry {
        return httpClient.submitFormWithBinaryData(
            url = "diary/entries",
            formData = formData {
                append("entry_date", request.entryDate)
                append("skin_condition", request.skinCondition.toString())
                request.hydrationLevel?.let { append("hydration_level", it.toString()) }
                request.problemZones?.let { append("problem_zones", it) }
                request.notes?.let { append("notes", it) }

                photoBytes?.let { bytes ->
                    append(
                        key = "photo",
                        value = bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"photo\"; filename=\"diary.jpg\"")
                        }
                    )
                }
            }
        ).body()
    }

    override suspend fun updateEntry(
        id: String,
        request: CreateDiaryEntryRequest,
        photoBytes: ByteArray?
    ): DiaryEntry {
        return httpClient.request("diary/entries/$id") {
            method = HttpMethod.Put
            setBody(MultiPartFormDataContent(formData {
                append("entry_date", request.entryDate)
                append("skin_condition", request.skinCondition.toString())
                request.hydrationLevel?.let { append("hydration_level", it.toString()) }
                request.problemZones?.let { append("problem_zones", it) }
                request.notes?.let { append("notes", it) }

                photoBytes?.let { bytes ->
                    append(
                        key = "photo",
                        value = bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"photo\"; filename=\"diary.jpg\"")
                        }
                    )
                }
            }))
        }.body()
    }

    override suspend fun deleteEntry(id: String) {
        httpClient.delete("diary/entries/$id")
    }
}