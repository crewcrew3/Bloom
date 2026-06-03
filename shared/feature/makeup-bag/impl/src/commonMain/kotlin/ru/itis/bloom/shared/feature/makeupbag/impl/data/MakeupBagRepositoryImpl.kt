package ru.itis.bloom.shared.feature.makeupbag.impl.data

import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagApi
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest

internal class MakeupBagRepositoryImpl(
    private val api: MakeupBagApi
) : MakeupBagRepository {

    override suspend fun getProducts(
        category: ProductCategory?,
        status: ProductStatus
    ): Result<List<Product>> = api.getProducts(category, status)

    override suspend fun getProductById(id: String): Result<Product> = api.getProductById(id)

    override suspend fun createProduct(request: CreateProductRequest, photoBytes: ByteArray?): Result<Product> =
        api.createProduct(request, photoBytes)

    override suspend fun updateProduct(id: String, request: CreateProductRequest, photoBytes: ByteArray?): Result<Product> =
        api.updateProduct(id, request, photoBytes)

    override suspend fun deleteProduct(id: String): Result<Unit> = api.deleteProduct(id)

    override suspend fun archiveProduct(id: String): Result<Product> = api.archiveProduct(id)
}