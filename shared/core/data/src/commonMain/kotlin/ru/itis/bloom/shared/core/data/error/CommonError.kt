package ru.itis.bloom.shared.core.data.error

sealed interface CommonError : BaseError {
    data object NetworkUnavailable : CommonError
    data object Timeout : CommonError
    data object Unknown : CommonError
}