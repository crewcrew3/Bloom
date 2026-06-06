package ru.itis.bloom.shared.core.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.itis.bloom.shared.core.domain.usecase.ImageUriToByteArrayUseCase
import ru.itis.bloom.shared.core.domain.usecase.ImageUriToByteArrayUseCaseImpl

actual val coreDomainModule: Module = module {
    single<ImageUriToByteArrayUseCase> {
        ImageUriToByteArrayUseCaseImpl()
    }
}