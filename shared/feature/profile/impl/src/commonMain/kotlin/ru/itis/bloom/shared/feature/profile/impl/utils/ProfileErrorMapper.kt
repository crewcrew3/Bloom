package ru.itis.bloom.shared.feature.profile.impl.utils

import ru.itis.bloom.shared.core.data.error.BaseError
import ru.itis.bloom.shared.core.data.error.CommonError

internal object ProfileErrorMapper {
    fun mapToMessageRes(error: BaseError): ProfileMessageRes {
        return when (error) {
            is ru.itis.bloom.shared.feature.profile.api.error.ProfileError -> ProfileMessageRes.fromProfileError(error)
            is CommonError -> when (error) {
                CommonError.Unauthorized -> ProfileMessageRes.Error.Unauthorized
                CommonError.NetworkUnavailable -> ProfileMessageRes.Error.Network
                CommonError.Timeout -> ProfileMessageRes.Error.Timeout
                CommonError.ServerError -> ProfileMessageRes.Error.ServerError
                else -> ProfileMessageRes.Error.Unknown
            }
            else -> ProfileMessageRes.Error.Unknown
        }
    }
}