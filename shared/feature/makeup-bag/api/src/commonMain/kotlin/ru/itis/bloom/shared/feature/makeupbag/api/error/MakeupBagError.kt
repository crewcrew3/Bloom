package ru.itis.bloom.shared.feature.makeupbag.api.error

import ru.itis.bloom.shared.core.data.error.BaseError

sealed interface MakeupBagError : BaseError {
    data object ProductLinkedToRoutine : MakeupBagError
    data object ProductNotFound : MakeupBagError
    data object InvalidCategory : MakeupBagError
    data object InvalidRating : MakeupBagError
    data object SavingPhotoError: MakeupBagError
}