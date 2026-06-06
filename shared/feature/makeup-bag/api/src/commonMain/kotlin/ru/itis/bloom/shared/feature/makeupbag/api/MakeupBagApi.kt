package ru.itis.bloom.shared.feature.makeupbag.api

import ru.itis.bloom.shared.core.data.Result as BloomResult
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

interface MakeupBagApi {
    suspend fun getProducts(
        category: ProductCategory?,
        status: ProductStatus
    ): BloomResult<List<Product>>

    suspend fun getProductById(id: String): BloomResult<Product>

    suspend fun createProduct(request: CreateProductRequest, photoBytes: ByteArray?): BloomResult<Product>

    suspend fun updateProduct(id: String, request: CreateProductRequest, photoBytes: ByteArray?): BloomResult<Product>

    suspend fun deleteProduct(id: String): BloomResult<Unit>

    suspend fun archiveProduct(id: String): BloomResult<Product>
}