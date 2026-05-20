package ru.itis.bloom.shared.feature.makeupbag.impl.di

import org.koin.core.module.dsl.viewModelOf
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
    single<MakeupBagApi> { MakeupBagApiImpl(httpClient = get()) }
    single<MakeupBagRepository> { MakeupBagRepositoryImpl(api = get()) }

    factory { GetProductsUseCase(repository = get()) }
    factory { GetProductByIdUseCase(repository = get()) }
    factory { CreateProductUseCase(repository = get()) }
    factory { UpdateProductUseCase(repository = get()) }
    factory { DeleteProductUseCase(repository = get()) }
    factory { ArchiveProductUseCase(repository = get()) }

    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductFormViewModel)
    viewModelOf(::ProductDetailViewModel)

    factory { MakeupBagNavigationHandler(get()) }
}