package ru.itis.bloom.shared.core.domain.usecase

interface ImageUriToByteArrayUseCase {
    suspend fun execute(uri: String, maxSizeBytes: Long = 10 * 1024 * 1024): Result<ByteArray>
}