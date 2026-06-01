package ru.itis.bloom.shared.core.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.itis.bloom.shared.core.domain.error.ImageTooLargeException
import ru.itis.bloom.shared.core.domain.error.UnsupportedImageFormatException
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

internal class ImageUriToByteArrayUseCaseImpl : ImageUriToByteArrayUseCase {

    override suspend fun execute(
        uri: String,
        maxSizeBytes: Long
    ): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            val file = File(URI(uri))

            if (!file.exists()) {
                throw IllegalArgumentException("File not found: $uri")
            }

            val originalBytes = file.readBytes()

            if (originalBytes.size > maxSizeBytes) {
                throw ImageTooLargeException(originalBytes.size, maxSizeBytes)
            }

            val extension = file.extension.lowercase()
            if (extension !in listOf("jpg", "jpeg", "png")) {
                throw UnsupportedImageFormatException(extension)
            }

            // Проверяем, что файл действительно изображение
            val image: BufferedImage = ImageIO.read(file)
                ?: throw UnsupportedImageFormatException(extension)

            // Компрессия PNG > 5MB → JPEG
            if (extension == "png" && originalBytes.size > 5 * 1024 * 1024) {
                compressToJpeg(image, maxSizeBytes)
            } else {
                originalBytes
            }
        }
    }

    private fun compressToJpeg(image: BufferedImage, maxSizeBytes: Long): ByteArray {
        // Масштабирование если > 1920px
        val scaledImage = if (image.width > 1920 || image.height > 1920) {
            val ratio = minOf(1920f / image.width, 1920f / image.height)
            val newWidth = (image.width * ratio).toInt()
            val newHeight = (image.height * ratio).toInt()
            val scaled = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
            scaled.graphics.drawImage(image, 0, 0, newWidth, newHeight, null)
            scaled
        } else {
            image
        }

        var quality = 0.9f
        val outputStream = ByteArrayOutputStream()

        while (quality >= 0.1f) {
            outputStream.reset()
            val writer = ImageIO.getImageWritersByFormatName("jpg").next()
            val writeParam = writer.defaultWriteParam
            writeParam.compressionMode = javax.imageio.ImageWriteParam.MODE_EXPLICIT
            writeParam.compressionQuality = quality

            writer.output = javax.imageio.stream.MemoryCacheImageOutputStream(outputStream)
            writer.write(null, javax.imageio.IIOImage(scaledImage, null, null), writeParam)
            writer.dispose()

            if (outputStream.size() <= maxSizeBytes) break
            quality -= 0.1f
        }

        val result = outputStream.toByteArray()
        if (result.size > maxSizeBytes) {
            throw ImageTooLargeException(result.size, maxSizeBytes)
        }
        return result
    }
}