package ru.itis.bloom.shared.feature.makeupbag.impl.utils

import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError
import bloom.shared.feature.makeup_bag.impl.generated.resources.*

internal sealed class MakeupBagMessageRes {
    abstract fun toResourceId(): StringResource

    sealed class Error : MakeupBagMessageRes() {
        data object ProductLinkedToRoutine : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_product_linked
        }
        data object ProductNotFound : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_not_found
        }
        data object InvalidCategory : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_invalid_category
        }
        data object Validation : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_validation
        }
        data object Network : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_network
        }
        data object Timeout : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_timeout
        }
        data object Unknown : Error() {
            override fun toResourceId(): StringResource = Res.string.makeup_error_unknown
        }
    }

    sealed class Success : MakeupBagMessageRes() {
        data object ProductAdded : Success() {
            override fun toResourceId(): StringResource = Res.string.makeup_success_added
        }
        data object ProductUpdated : Success() {
            override fun toResourceId(): StringResource = Res.string.makeup_success_updated
        }
        data object ProductDeleted : Success() {
            override fun toResourceId(): StringResource = Res.string.makeup_success_deleted
        }
        data object ProductArchived : Success() {
            override fun toResourceId(): StringResource = Res.string.makeup_success_archived
        }
    }

    sealed class Validation : MakeupBagMessageRes() {
        data object NameRequired : Validation() {
            override fun toResourceId(): StringResource = Res.string.makeup_validation_name_required
        }
        data object NameTooLong : Validation() {
            override fun toResourceId(): StringResource = Res.string.makeup_validation_name_too_long
        }
        data object BrandTooLong : Validation() {
            override fun toResourceId(): StringResource = Res.string.makeup_validation_brand_too_long
        }
        data object ReviewTooLong : Validation() {
            override fun toResourceId(): StringResource = Res.string.makeup_validation_review_too_long
        }
        data object ShelfLifeInvalid : Validation() {
            override fun toResourceId(): StringResource = Res.string.makeup_validation_shelf_life_invalid
        }
    }

    companion object {
        fun fromMakeupBagError(error: MakeupBagError): Error {
            return when (error) {
                is MakeupBagError.ProductLinkedToRoutine -> Error.ProductLinkedToRoutine
                is MakeupBagError.ProductNotFound -> Error.ProductNotFound
                is MakeupBagError.InvalidCategory -> Error.InvalidCategory
                is MakeupBagError.InvalidRating -> Error.Validation
            }
        }
    }
}