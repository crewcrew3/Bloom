package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bloom.shared.feature.makeup_bag.impl.generated.resources.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bloom.shared.core.data.Result
import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.domain.usecase.ImageUriToByteArrayUseCase
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError
import ru.itis.bloom.shared.feature.makeupbag.api.model.request.CreateProductRequest
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.impl.domain.usecase.*
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagErrorMapper
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MakeupBagMessageRes
import ru.itis.bloom.shared.feature.makeupbag.impl.utils.MockProducts

private const val TAG = "BLOOM_PRODUCT_FORM_VM"

internal class ProductFormViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val archiveProductUseCase: ArchiveProductUseCase,
    private val imageUriToByteArrayUseCase: ImageUriToByteArrayUseCase,
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
                is ProductFormIntent.FinishedChanged -> _state.update { it.copy(form = it.form.copy(isFinished = intent.isFinished)) }

                is ProductFormIntent.Submit -> submit()
                is ProductFormIntent.Archive -> archive()
                is ProductFormIntent.Delete -> delete()
                is ProductFormIntent.NavigateBack -> _effect.emit(ProductFormEffect.NavigateBack)

                is ProductFormIntent.RequestPhotoSelection -> processPhotoUri(intent.uri)
                is ProductFormIntent.RemovePhoto -> {
                    _state.update {
                        it.copy(
                            form = it.form.copy(
                                photoBytes = null,
                                photoUri = null
                            )
                        )
                    }
                }
                //is ProductFormIntent.ClearErrors -> _state.update { it.copy(form = it.form.copy(nameError = null, brandError = null, reviewError = null, shelfLifeError = null, photoError = null)) }
            }
        }
    }

    private suspend fun loadProduct(id: String) {
        _state.update { it.copy(isLoading = true) }

        //mockTest(id)

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
                            brand = p.brand,
                            category = p.category,
                            inciComposition = p.inciComposition,
                            personalRating = p.personalRating,
                            personalReview = p.personalReview,
                            openedDate = p.openedDate,
                            shelfLifeAfterOpening = p.shelfLifeAfterOpening,
                            photoUri = p.photoUrl,
                            isFinished = p.status == ProductStatus.Archived,
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
        if (_state.value.isPhotoProcessing) return
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
            updateProductUseCase(safeId, request, form.photoBytes)
        } ?: createProductUseCase(request, form.photoBytes)

        when (result) {
            is Result.Success -> {
                if (form.isFinished && _state.value.productId != null) {
                    archive()
                } else {
                    _effect.emit(
                        ProductFormEffect.ShowMessage(
                            if (_state.value.productId != null) MakeupBagMessageRes.Success.ProductUpdated else MakeupBagMessageRes.Success.ProductAdded
                        )
                    )
                    _state.update { it.copy(isLoading = false) }
                    //_effect.emit(ProductFormEffect.NavigateBackAndRefresh)
                    _effect.emit(ProductFormEffect.NavigateBack)
                }
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
                _effect.emit(ProductFormEffect.ShowMessage(MakeupBagMessageRes.Success.ProductArchived))
                _effect.emit(ProductFormEffect.NavigateBackToRoot)
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
                _effect.emit(ProductFormEffect.NavigateBackToRoot)
                _effect.emit(ProductFormEffect.ShowMessage(MakeupBagMessageRes.Success.ProductDeleted))
            }
            is Result.Error -> handleError(result.error)
            is Result.Loading -> _state.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun processPhotoUri(uri: String) {
        _state.update { it.copy(isPhotoProcessing = true, form = it.form.copy(photoError = null)) }

        imageUriToByteArrayUseCase.execute(uri)
            .onSuccess { bytes ->
                _state.update {
                    it.copy(
                        isPhotoProcessing = false,
                        form = it.form.copy(
                            photoBytes = bytes,
                            photoUri = null,
                            photoError = null
                        )
                    )
                }
            }
            .onFailure {
                _state.update {
                    it.copy(
                        isPhotoProcessing = false,
                        form = it.form.copy(photoError = Res.string.makeup_error_saving_photo)
                    )
                }
                val messageRes = MakeupBagErrorMapper.mapToMessageRes(MakeupBagError.SavingPhotoError)
                _effect.emit(
                    ProductFormEffect.ShowMessage(
                        messageRes
                    )
                )
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

    private suspend fun mockTest(id: String) {
        //для теста
        delay(3000)
        val p = MockProducts.getById(id)
        p?.let {
            _state.update {
                it.copy(
                    productId = p.id,
                    product = p,
                    isLoading = false,
                    form = ProductFormState.FormFields(
                        name = p.name,
                        brand = p.brand,
                        category = p.category,
                        inciComposition = p.inciComposition,
                        personalRating = p.personalRating,
                        personalReview = p.personalReview,
                        openedDate = p.openedDate,
                        shelfLifeAfterOpening = p.shelfLifeAfterOpening,
                        photoUri = p.photoUrl,
                        isFinished = p.status == ProductStatus.Archived,
                    )
                )
            }
        }
    }
}