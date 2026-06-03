package ru.itis.bloom.shared.core.domain.error

class ImageTooLargeException(actualSize: Int, maxSize: Long) :
    Exception("Фото слишком большое: ${actualSize / 1024} КБ (макс. ${maxSize / 1024 / 1024} МБ)")