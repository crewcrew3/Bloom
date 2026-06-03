package ru.itis.bloom.shared.core.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import ru.itis.bloom.shared.core.domain.error.ImageTooLargeException
import ru.itis.bloom.shared.core.domain.error.UnsupportedImageFormatException

internal class ImageUriToByteArrayUseCaseImpl(
    private val context: Context
): ImageUriToByteArrayUseCase{
    override suspend fun execute(
        uri: String,
        maxSizeBytes: Long
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            val inputStream = context.contentResolver.openInputStream(uri.toUri())
                ?: throw IllegalArgumentException("Cannot open URI: $uri")

            inputStream.use { stream ->
                // Сначала читаем в ByteArray для проверки размера
                val originalBytes = stream.readBytes()

                if (originalBytes.size > maxSizeBytes) {
                    throw ImageTooLargeException(originalBytes.size, maxSizeBytes)
                }

                // Проверяем и конвертируем в JPEG если нужно
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size, options)

                val mimeType = options.outMimeType
                if (mimeType !in listOf("image/jpeg", "image/jpg", "image/png")) {
                    throw UnsupportedImageFormatException(mimeType)
                }

                // Если PNG > 5MB — конвертируем в JPEG с компрессией
                if (mimeType == "image/png" && originalBytes.size > 5 * 1024 * 1024) {
                    compressToJpeg(originalBytes, maxSizeBytes)
                } else {
                    originalBytes
                }
            }
        }
    }

    private fun compressToJpeg(originalBytes: ByteArray, maxSizeBytes: Long): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
            ?: throw IllegalStateException("Cannot decode bitmap")

        // Масштабируем если нужно
        val scaledBitmap = if (bitmap.width > 1920 || bitmap.height > 1920) {
            val ratio = minOf(1920f / bitmap.width, 1920f / bitmap.height)
            bitmap.scale((bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt())
        } else {
            bitmap
        }

        // Компрессия с постепенным уменьшением качества
        var quality = 90
        val outputStream = ByteArrayOutputStream()

        while (quality >= 10) {
            outputStream.reset()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            if (outputStream.size() <= maxSizeBytes) break
            quality -= 10
        }

        if (!scaledBitmap.isRecycled) scaledBitmap.recycle()

        val result = outputStream.toByteArray()
        if (result.size > maxSizeBytes) {
            throw ImageTooLargeException(result.size, maxSizeBytes)
        }
        return result
    }
}