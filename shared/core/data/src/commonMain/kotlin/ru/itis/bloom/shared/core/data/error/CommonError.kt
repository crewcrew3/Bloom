package ru.itis.bloom.shared.core.data.error

sealed interface CommonError : BaseError {
    data object NetworkUnavailable : CommonError
    data object Timeout : CommonError
    data object ValidationError : CommonError
    data object Unknown : CommonError
    data object Unauthorized : CommonError
    data object Forbidden : CommonError
    data object NotFound : CommonError
    data object Conflict : CommonError
    data object ServerError : CommonError
}