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
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.*
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagErrorMapper
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
                is ProductFormIntent.BrandChanged -> _state.update { it.copy(form = it.form.copy(brand = intent.brand, brandError = null)) }
                is ProductFormIntent.CategoryChanged -> _state.update { it.copy(form = it.form.copy(category = intent.category)) }
                is ProductFormIntent.InciChanged -> _state.update { it.copy(form = it.form.copy(inciComposition = intent.inci)) }
                is ProductFormIntent.RatingChanged -> _state.update { it.copy(form = it.form.copy(personalRating = intent.rating)) }
                is ProductFormIntent.ReviewChanged -> _state.update { it.copy(form = it.form.copy(personalReview = intent.review, reviewError = null)) }
                is ProductFormIntent.OpenedDateChanged -> _state.update { it.copy(form = it.form.copy(openedDate = intent.date)) }
                is ProductFormIntent.ShelfLifeChanged -> _state.update { it.copy(form = it.form.copy(shelfLifeAfterOpening = intent.months, shelfLifeError = null)) }

                is ProductFormIntent.Submit -> submit()
                is ProductFormIntent.Archive -> archive()
                is ProductFormIntent.Delete -> delete()
                is ProductFormIntent.NavigateBack -> _effect.emit(ProductFormEffect.NavigateBack)
                is ProductFormIntent.ClearErrors -> _state.update { it.copy(form = it.form.copy(nameError = null, brandError = null, reviewError = null, shelfLifeError = null)) }
            }
        }
    }

    private suspend fun loadProduct(id: String) {
        _state.update { it.copy(isLoading = true) }
        when (val result = getProductByIdUseCase(id)) {
            is Result.Success -> {
                val p = result.data
                _state.update {
                    it.copy( //TODO("оставить наллы где могут быть наллы(без дефолт значений), а в компоузе уже сеттить значения по умолчанию через строки")
                        productId = p.id,
                        product = p,
                        isLoading = false,
                        form = ProductFormState.FormFields(
                            name = p.name,
                            brand = p.brand ?: "",
                            category = p.category,
                            inciComposition = p.inciComposition ?: "",
                            personalRating = p.personalRating ?: 0,
                            personalReview = p.personalReview ?: "",
                            openedDate = p.openedDate ?: "",
                            shelfLifeAfterOpening = p.shelfLifeAfterOpening ?: 0
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
        if (!validateForm(form)) return
        _state.update { it.copy(isLoading = true) }
        val request = CreateProductRequest(
            name = form.name,
            brand = form.brand?.ifBlank { null },
            category = form.category,
            inciComposition = form.inciComposition?.ifBlank { null },
            personalRating = form.personalRating,
            personalReview = form.personalReview?.ifBlank { null },
            openedDate = form.openedDate?.ifBlank { null },
            shelfLifeAfterOpening = form.shelfLifeAfterOpening
        )

        val result = _state.value.productId?.let { safeId ->
            updateProductUseCase(safeId, request)
        } ?: createProductUseCase(request)

        when (result) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false) }
                //_effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.NavigateBack)
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
                _state.update { it.copy(isLoading = false) }
                //_effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.NavigateBack)
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
                _state.update { it.copy(isLoading = false) }
                //_effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                _effect.emit(ProductFormEffect.NavigateBack)
                _effect.emit(ProductFormEffect.ShowMessage(MakeupBagMessageRes.Success.ProductDeleted))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private fun validateForm(form: ProductFormState.FormFields): Boolean {
        var ok = true

        with(form) {
            // Name: required, max 200
            if (name.isBlank()) {
                _state.update { it.copy(form = it.form.copy(nameError = MakeupBagMessageRes.Validation.NameRequired.toResourceId())) }
                ok = false
            } else if (name.length > 200) {
                _state.update { it.copy(form = it.form.copy(nameError = MakeupBagMessageRes.Validation.NameTooLong.toResourceId())) }
                ok = false
            }

            // Brand: optional, max 200 if present
            if (!brand.isNullOrBlank() && brand.length > 200) {
                _state.update { it.copy(form = it.form.copy(brandError = MakeupBagMessageRes.Validation.BrandTooLong.toResourceId())) }
                ok = false
            }

            // ShelfLifeAfterOpening: optional, must be > 0 if present
            if (shelfLifeAfterOpening != null && shelfLifeAfterOpening <= 0) {
                _state.update { it.copy(form = it.form.copy(shelfLifeError = MakeupBagMessageRes.Validation.ShelfLifeInvalid.toResourceId())) }
                ok = false
            }

            // PersonalReview: optional, max 1000 if present
            if (!personalReview.isNullOrBlank() && personalReview.length > 1000) {
                _state.update { it.copy(form = it.form.copy(reviewError = MakeupBagMessageRes.Validation.ReviewTooLong.toResourceId())) }
                ok = false
            }

            // PersonalRating: optional, 1-5 if present
//            if (personalRating != null && personalRating !in 1..5) {
//                _state.update { it.copy(form = it.form.copy(ratingError = MakeupBagMessageRes.Validation.RatingInvalid.toResourceId())) }
//                ok = false
//            }

            // OpenedDate: optional, format YYYY-MM-DD if present
//            if (openedDate.isNotBlank() && !openedDate.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
//                _state.update { it.copy(form = it.form.copy(openedDateError = MakeupBagMessageRes.Validation.DateInvalid.toResourceId())) }
//                ok = false
//            }
        }
        return ok
    }

    private suspend fun handleError(error: BaseError) {
        val messageRes = MakeupBagErrorMapper.mapToMessageRes(error)
        _state.update {
            it.copy(isLoading = false)
        }
        _effect.emit(ProductFormEffect.ShowMessage(messageRes))
    }
}