package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.GetProductsUseCase
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

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
                is ProductListIntent.ClearErrors -> {
                    _state.update { it.copy(generalError = null) }
                }
            }
        }
    }

    private suspend fun loadProducts() {
        _state.update { it.copy(isLoading = true, generalError = null) }
        when (val result = getProductsUseCase(
            category = _state.value.filterCategory,
            status = ProductStatus.Active
        )) {
            is Result.Success -> _state.update {
                it.copy(products = result.data, isLoading = false)
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = mapErrorToMessageRes(error)
        _state.update {
            it.copy(isLoading = false, generalError = if (messageRes is MakeupBagMessageRes.Error) messageRes.toResourceId() else null)
        }
        _effect.emit(ProductListEffect.ShowMessage(messageRes))
    }

    private fun mapErrorToMessageRes(error: BaseError): MakeupBagMessageRes {
        return when (error) {
            is MakeupBagError -> MakeupBagMessageRes.fromMakeupBagError(error)
            is CommonError -> when (error) {
                is CommonError.ValidationError -> MakeupBagMessageRes.Error.Validation
                is CommonError.NetworkUnavailable -> MakeupBagMessageRes.Error.Network
                is CommonError.Timeout -> MakeupBagMessageRes.Error.Timeout
                else -> MakeupBagMessageRes.Error.Unknown
            }
            else -> MakeupBagMessageRes.Error.Unknown
        }
    }
}