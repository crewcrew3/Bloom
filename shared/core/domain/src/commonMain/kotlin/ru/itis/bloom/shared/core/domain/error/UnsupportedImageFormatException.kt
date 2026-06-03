package ru.itis.bloom.shared.core.domain.error

class UnsupportedImageFormatException(mimeType: String?) :
    Exception("Неподдерживаемый формат: $mimeType. Используйте JPEG или PNG.")