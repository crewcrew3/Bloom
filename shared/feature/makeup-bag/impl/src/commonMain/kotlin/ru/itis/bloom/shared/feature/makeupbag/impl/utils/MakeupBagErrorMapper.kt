package ru.itis.bloom.shared.feature.makeupbag.impl.utils

import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError
import ru.itis.bloom.shared.feature.makeupbag.api.error.MakeupBagError

internal object MakeupBagErrorMapper {
    fun mapToMessageRes(error: BaseError): MakeupBagMessageRes {
        return when (error) {
            is MakeupBagError -> MakeupBagMessageRes.fromMakeupBagError(error)
            is CommonError -> when (error) {
                CommonError.ValidationError -> MakeupBagMessageRes.Error.Validation
                CommonError.NetworkUnavailable -> MakeupBagMessageRes.Error.Network
                CommonError.Timeout -> MakeupBagMessageRes.Error.Timeout
                CommonError.Unauthorized -> MakeupBagMessageRes.Error.Unauthorized
                CommonError.Forbidden -> MakeupBagMessageRes.Error.Forbidden
                CommonError.ServerError -> MakeupBagMessageRes.Error.ServerError
                else -> MakeupBagMessageRes.Error.Unknown
            }
            else -> MakeupBagMessageRes.Error.Unknown
        }
    }
}