package ru.itis.bloom.shared.feature.makeupbag.impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagApi
import ru.itis.bloom.shared.feature.makeupbag.api.MakeupBagRepository
import ru.itis.bloom.shared.feature.makeupbag.impl.data.MakeupBagRepositoryImpl
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.*
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.navigation.MakeupBagNavigationHandler
import ru.itis.bloom.shared.feature.makeupbag.impl.network.MakeupBagApiImpl

val makeupBagModule = module {
    singleOf(::MakeupBagApiImpl) bind MakeupBagApi::class
    singleOf(::MakeupBagRepositoryImpl) bind MakeupBagRepository::class

    singleOf(::GetProductsUseCase)
    singleOf(::GetProductByIdUseCase)
    singleOf(::CreateProductUseCase)
    singleOf(::UpdateProductUseCase)
    singleOf(::DeleteProductUseCase)
    singleOf(::ArchiveProductUseCase)

    singleOf(::MakeupBagNavigationHandler)

    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductFormViewModel)
    viewModelOf(::ProductDetailViewModel)
}