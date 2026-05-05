package ru.itis.bloom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform