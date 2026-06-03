package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.GetProductsUseCase
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagErrorMapper
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MockProducts

internal class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductListEffect>(extraBufferCapacity = 10)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: ProductListIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProductListIntent.LoadProducts -> loadProducts()
                is ProductListIntent.FilterByCategory -> {
                    _state.update { it.copy(filterCategory = intent.category) }
                    loadProducts()
                }
                is ProductListIntent.SelectProduct -> {
                    _effect.emit(ProductListEffect.NavigateToProductDetail(intent.productId))
                }
                is ProductListIntent.NavigateToCreate -> {
                    _effect.emit(ProductListEffect.NavigateToCreateScreen)
                }

                is ProductListIntent.Refresh -> {
                    loadProducts(isRefresh = true)
                }
            }
        }
    }

    private suspend fun loadProducts(isRefresh: Boolean = false) {
        _state.update { it.copy(isLoading = !isRefresh, isRefreshing = isRefresh) }

        //mockTest()

        // раскомментировать
        when (val result = getProductsUseCase(
            category = _state.value.filterCategory,
            status = ProductStatus.Active
        )) {
            is Result.Success -> _state.update {
                it.copy(products = result.data, isLoading = false, isRefreshing = false)
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = !isRefresh, isRefreshing = isRefresh) }
        }
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = MakeupBagErrorMapper.mapToMessageRes(error)
        _state.update {
            it.copy(isLoading = false, isRefreshing = false)
        }
        _effect.emit(ProductListEffect.ShowMessage(messageRes))
    }

    private suspend fun mockTest() {
        //для теста
        delay(3000)
        val category = _state.value.filterCategory
        val filteredProducts = MockProducts.getByCategory(category)
        _state.update {
            it.copy(
                products = filteredProducts,
                isLoading = false
            )
        }
    }
}