package ru.itis.bloom.shared.feature.profile.api.error

import ru.itis.bloom.shared.core.data.error.BaseError

sealed interface ProfileError : BaseError {
    data object ProfileNotFound : ProfileError
}