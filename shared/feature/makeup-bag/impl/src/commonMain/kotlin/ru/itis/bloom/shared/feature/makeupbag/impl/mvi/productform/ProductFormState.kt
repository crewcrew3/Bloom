package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal data class ProductFormState(
    val productId: String? = null,
    val product: Product? = null,
    val isLoading: Boolean = false,
    val form: FormFields = FormFields()
) {
    data class FormFields(
        val name: String = "",
        val brand: String? = null,
        val category: ProductCategory = ProductCategory.Other,
        val inciComposition: String? = null,
        val personalRating: Int? = null,
        val personalReview: String? = null,
        val openedDate: String? = null,
        val shelfLifeAfterOpening: Int? = null,
        val photoUri: String? = null,
        val isFinished: Boolean = false, //архив

        // Ошибки валидации по полям
        val nameError: StringResource? = null,
        val brandError: StringResource? = null,
        val reviewError: StringResource? = null,
        val shelfLifeError: StringResource? = null
        //val categoryError: StringResource? = null,
        //val ratingError: StringResource? = null,
        //val openedDateError: StringResource? = null,
    ) {
        val isValid: Boolean
            get() = name.isNotBlank() && name.length <= 200 &&
                    (brand?.let {
                        (it.isNotBlank() && it.length <= 200) || it.isBlank()
                    } ?: true) &&
                    (personalRating == null || personalRating in 1..5) &&
                    (personalReview?.let {
                        (it.isNotBlank() && it.length <= 1000) || it.isBlank()
                    } ?: true) &&
                    //openedDate &&
                    (shelfLifeAfterOpening?.let {
                        it > 0
                    } ?: true) &&
                    nameError == null && brandError == null &&
                    shelfLifeError == null &&
                    reviewError == null
    }
}