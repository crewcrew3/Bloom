package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail

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
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.*
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagErrorMapper
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

internal class ProductDetailViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val archiveProductUseCase: ArchiveProductUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductDetailEffect>(extraBufferCapacity = 10)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: ProductDetailIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProductDetailIntent.LoadProduct -> loadProduct(intent.productId)
                is ProductDetailIntent.NavigateToEdit -> {
                    _state.value.product?.id?.let { _effect.emit(ProductDetailEffect.NavigateToEdit(it)) }
                }
                is ProductDetailIntent.Archive -> archive()
                is ProductDetailIntent.Delete -> delete()
                is ProductDetailIntent.NavigateBack -> _effect.emit(ProductDetailEffect.NavigateBack)
            }
        }
    }

    private suspend fun loadProduct(id: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = getProductByIdUseCase(id)) {
            is Result.Success -> _state.update { it.copy(product = result.data, isLoading = false) }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun archive() {
        val id = _state.value.product?.id ?: return
        _state.update { it.copy(isLoading = true) }
        when (val result = archiveProductUseCase(id)) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProductDetailEffect.NavigateBack)
                _effect.emit(ProductDetailEffect.ShowMessage(MakeupBagMessageRes.Success.ProductArchived))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun delete() {
        val id = _state.value.product?.id ?: return
        _state.update { it.copy(isLoading = true) }
        when (val result = deleteProductUseCase(id)) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProductDetailEffect.NavigateBack)
                _effect.emit(ProductDetailEffect.ShowMessage(MakeupBagMessageRes.Success.ProductDeleted))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = MakeupBagErrorMapper.mapToMessageRes(error)
        _state.update {
            it.copy(isLoading = false)
        }
        _effect.emit(ProductDetailEffect.ShowMessage(messageRes))
    }
}