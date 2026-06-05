package ru.itis.bloom.shared.feature.makeupbag.impl.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.*
import io.ktor.http.content.PartData
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagApi
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

internal class MakeupBagApiImpl(
    private val httpClient: HttpClient
) : MakeupBagApi {

    override suspend fun getProducts(
        category: ProductCategory?,
        status: ProductStatus
    ): Result<List<Product>> {
        return try {
            val response = httpClient.get("cosmetics/products") {
                category?.let { parameter("category", it.name.lowercase()) }
                parameter("status", status.name.lowercase())
            }
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            val response = httpClient.get("cosmetics/products/$id")
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun createProduct(
        request: CreateProductRequest,
        photoBytes: ByteArray?
    ): Result<Product> {
        return try {
            val response = httpClient.submitFormWithBinaryData(
                url = "cosmetics/products",
                formData = buildFormData(request, photoBytes)
            )
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun updateProduct(
        id: String,
        request: CreateProductRequest,
        photoBytes: ByteArray?
    ): Result<Product> {
        return try {
            val response = httpClient.submitFormWithBinaryData(
                url = "cosmetics/products/$id",
                formData = buildFormData(request, photoBytes)
            ) {
                method = HttpMethod.Put
            }
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            val response = httpClient.delete("cosmetics/products/$id")
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun archiveProduct(id: String): Result<Product> {
        return try {
            val response = httpClient.patch("cosmetics/products/$id/archive")
            Result.Success(response.body())
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    private fun buildFormData(
        request: CreateProductRequest,
        photoBytes: ByteArray?
    ): List<PartData> {
        return formData {
            append("name", request.name)
            request.brand?.let { append("brand", it) }
            append("category", request.category.name.lowercase())
            request.inciComposition?.let { append("inci_composition", it) }
            request.personalRating?.let { append("personal_rating", it.toString()) }
            request.personalReview?.let { append("personal_review", it) }
            request.openedDate?.let { append("opened_date", it) }
            request.shelfLifeAfterOpening?.let {
                append(
                    "shelf_life_after_opening",
                    it.toString()
                )
            }

            photoBytes?.let { bytes ->
                append(
                    key = "photo",
                    value = bytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"cosmetics.jpg\"")
                    }
                )
            }
        }
    }


    private fun mapToMakeupBagError(e: Exception): BaseError {
        return when (e) {
            is ClientRequestException -> when (e.response.status.value) {
                400 -> CommonError.ValidationError
                401 -> CommonError.Unauthorized
                403 -> CommonError.Forbidden
                404 -> MakeupBagError.ProductNotFound
                409 -> MakeupBagError.ProductLinkedToRoutine
                in 500..599 -> CommonError.ServerError
                else -> CommonError.Unknown
            }
            is HttpRequestTimeoutException -> CommonError.Timeout
            else -> CommonError.NetworkUnavailable
        }
    }
}
