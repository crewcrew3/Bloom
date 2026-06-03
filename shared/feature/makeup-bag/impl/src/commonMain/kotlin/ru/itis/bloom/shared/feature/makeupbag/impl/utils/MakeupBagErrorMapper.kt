package ru.itis.bloom.shared.feature.makeupbag.impl.utils

import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError

internal object MakeupBagErrorMapper {
    fun mapToMessageRes(error: BaseError): MakeupBagMessageRes {
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