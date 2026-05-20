package ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform

import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory

internal data class ProductFormState(
    val productId: String? = null,
    val product: Product? = null,
    val isLoading: Boolean = false,
    val generalError: StringResource? = null,
    val form: FormFields = FormFields()
) {
    data class FormFields(
        val name: String = "",
        val brand: String = "",
        val category: ProductCategory? = ProductCategory.Other,
        val inciComposition: String = "",
        val personalRating: Int? = null,
        val personalReview: String = "",
        val openedDate: String? = null,
        val shelfLifeAfterOpening: Int? = null,

        // Ошибки валидации по полям
        val nameError: StringResource? = null,
        val brandError: StringResource? = null,
        val categoryError: StringResource? = null,
        val ratingError: StringResource? = null,
        val reviewError: StringResource? = null,
        val openedDateError: StringResource? = null,
        val shelfLifeError: StringResource? = null
    ) {
        val isValid: Boolean
            get() = name.isNotBlank() && name.length <= 200 &&
                    brand.length <= 200 &&
                    category != null &&
                    (personalRating == null || personalRating in 1..5) &&
                    personalReview.length <= 1000 &&
                    nameError == null && brandError == null &&
                    categoryError == null && ratingError == null &&
                    reviewError == null
    }
}