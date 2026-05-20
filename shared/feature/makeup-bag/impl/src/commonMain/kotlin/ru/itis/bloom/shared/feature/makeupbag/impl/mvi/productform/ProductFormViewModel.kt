package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

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
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.*
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes

internal class ProductFormViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val archiveProductUseCase: ArchiveProductUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductFormState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductFormEffect>(extraBufferCapacity = 10)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: ProductFormIntent) {
        viewModelScope.launch {
            when (intent) {
                is ProductFormIntent.LoadProduct -> loadProduct(intent.productId)

                is ProductFormIntent.NameChanged -> _state.update { it.copy(form = it.form.copy(name = intent.name, nameError = null)) }
                is ProductFormIntent.BrandChanged -> _state.update { it.copy(form = it.form.copy(brand = intent.brand)) }
                is ProductFormIntent.CategoryChanged -> _state.update { it.copy(form = it.form.copy(category = intent.category, categoryError = null)) }
                is ProductFormIntent.InciChanged -> _state.update { it.copy(form = it.form.copy(inciComposition = intent.inci)) }
                is ProductFormIntent.RatingChanged -> _state.update { it.copy(form = it.form.copy(personalRating = intent.rating)) }
                is ProductFormIntent.ReviewChanged -> _state.update { it.copy(form = it.form.copy(personalReview = intent.review)) }
                is ProductFormIntent.OpenedDateChanged -> _state.update { it.copy(form = it.form.copy(openedDate = intent.date)) }
                is ProductFormIntent.ShelfLifeChanged -> _state.update { it.copy(form = it.form.copy(shelfLifeAfterOpening = intent.months)) }

                is ProductFormIntent.Submit -> submit()
                is ProductFormIntent.Archive -> archive()
                is ProductFormIntent.Delete -> delete()
                is ProductFormIntent.NavigateBack -> _effect.emit(ProductFormEffect.NavigateBack)
                is ProductFormIntent.ClearErrors -> _state.update { it.copy(generalError = null, form = it.form.copy(nameError = null, categoryError = null)) }
                is ProductFormIntent.ClearForm -> _state.update { it.copy(form = ProductFormState.FormFields()) }
            }
        }
    }

    private suspend fun loadProduct(id: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = getProductByIdUseCase(id)) {
            is Result.Success -> {
                val p = result.data
                _state.update {
                    it.copy(
                        productId = p.id,
                        product = p,
                        isLoading = false,
                        form = ProductFormState.FormFields(
                            name = p.name,
                            brand = p.brand ?: "",
                            category = p.category,
                            inciComposition = p.inciComposition ?: "",
                            personalRating = p.personalRating,
                            personalReview = p.personalReview ?: "",
                            openedDate = p.openedDate,
                            shelfLifeAfterOpening = p.shelfLifeAfterOpening
                        )
                    )
                }
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun submit() {
        val form = _state.value.form
        if (!form.isValid) return
        _state.update { it.copy(isLoading = true) }
        val request = CreateProductRequest(
            name = form.name,
            brand = form.brand.ifBlank { null },
            category = form.category!!,
            inciComposition = form.inciComposition.ifBlank { null },
            personalRating = form.personalRating,
            personalReview = form.personalReview.ifBlank { null },
            openedDate = form.openedDate,
            shelfLifeAfterOpening = form.shelfLifeAfterOpening
        )
        val result = if (_state.value.productId != null) {
            updateProductUseCase(_state.value.productId!!, request)
        } else {
            createProductUseCase(request)
        }
        when (result) {
            is Result.Success -> {
                _effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.ShowMessage(
                    if (_state.value.productId != null) MakeupBagMessageRes.Success.ProductUpdated else MakeupBagMessageRes.Success.ProductAdded
                ))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun archive() {
        val id = _state.value.productId ?: return
        _state.update { it.copy(isLoading = true) }
        when (val result = archiveProductUseCase(id)) {
            is Result.Success -> {
                _effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.ShowMessage(MakeupBagMessageRes.Success.ProductArchived))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun delete() {
        val id = _state.value.productId ?: return
        _state.update { it.copy(isLoading = true) }
        when (val result = deleteProductUseCase(id)) {
            is Result.Success -> {
                _effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.ShowMessage(MakeupBagMessageRes.Success.ProductDeleted))
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
        _effect.emit(ProductFormEffect.ShowMessage(messageRes))
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