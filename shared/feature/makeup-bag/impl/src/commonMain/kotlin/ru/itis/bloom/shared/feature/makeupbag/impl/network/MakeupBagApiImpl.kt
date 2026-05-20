package ru.itis.bloom.shared.feature.makeupbag.impl.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.PartData
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.apiCall
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagApi
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

internal class MakeupBagApiImpl(private val httpClient: HttpClient) : MakeupBagApi {

    override suspend fun getProducts(
        category: ProductCategory?,
        status: ProductStatus
    ): Result<List<Product>> = apiCall {
        httpClient.get("/cosmetics/products") {
            category?.let { parameter("category", it.name.lowercase()) }
            parameter("status", status.name.lowercase())
        }.body()
    }

    override suspend fun getProductById(id: String): Result<Product> = apiCall {
        httpClient.get("/cosmetics/products/$id").body()
    }

    override suspend fun createProduct(request: CreateProductRequest): Result<Product> = apiCall {
        httpClient.post("/cosmetics/products") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
//        return try {
//            val response = httpClient.submitFormWithBinaryData(
//                url = "/cosmetics/products",
//                formData = buildFormData(request)
//            )
//            when (response.status.value) {
//                400 -> Result.Error(CommonError.ValidationError)
//                else -> Result.Success(response.body())
//            }
//        } catch (e: Exception) {
//            Result.Error(mapToMakeupBagError(e))
//        }
    }

    override suspend fun updateProduct(id: String, request: CreateProductRequest): Result<Product> {
        return try {
            httpClient.put("/cosmetics/products/$id") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        } catch (e: Exception) {
            when (e) {
                is ClientRequestException -> when (e.response.status.value) {
                    404 -> Result.Error(MakeupBagError.ProductNotFound)
                    else -> Result.Error(CommonError.Unknown)
                }
                else -> Result.Error(CommonError.Unknown)
            }
        }
        /*return try {
            val response = httpClient.submitFormWithBinaryData(
                url = "/cosmetics/products/$id",
                formData = buildFormData(request)
            ) {
                method = HttpMethod.Put
            }
            when (response.status.value) {
                400 -> Result.Error(CommonError.ValidationError)
                404 -> Result.Error(MakeupBagError.ProductNotFound)
                else -> Result.Success(response.body())
            }
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }*/
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            val response = httpClient.delete("/cosmetics/products/$id")
            when (response.status.value) {
                204 -> Result.Success(Unit)
                404 -> Result.Error(MakeupBagError.ProductNotFound)
                409 -> Result.Error(MakeupBagError.ProductLinkedToRoutine)
                else -> Result.Error(CommonError.Unknown)
            }
        } catch (e: Exception) {
            Result.Error(mapToMakeupBagError(e))
        }
    }

    override suspend fun archiveProduct(id: String): Result<Product> = apiCall {
        httpClient.patch("/cosmetics/products/$id/archive").body()
    }

//    private fun buildFormData(request: CreateProductRequest): List<PartData> {
//        return formData {
//            append("name", request.name)
//            request.brand?.let { append("brand", it) }
//            append("category", request.category.name.lowercase())
//            request.inciComposition?.let { append("inci_composition", it) }
//            request.personalRating?.let { append("personal_rating", it.toString()) }
//            request.personalReview?.let { append("personal_review", it) }
//            request.openedDate?.let { append("opened_date", it) }
//            request.shelfLifeAfterOpening?.let {
//                append(
//                    "shelf_life_after_opening",
//                    it.toString()
//                )
//            }
//            //request.photo?.let { appendInput("photo", it) }
//        }
//    }


    private fun mapToMakeupBagError(e: Exception): BaseError {
        return when (e) {
            is ClientRequestException -> when (e.response.status.value) {
                404 -> MakeupBagError.ProductNotFound
                409 -> MakeupBagError.ProductLinkedToRoutine
                else -> CommonError.Unknown
            }
            is HttpRequestTimeoutException -> CommonError.Timeout
            else -> CommonError.NetworkUnavailable
        }
    }
}
