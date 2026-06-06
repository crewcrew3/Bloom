package ru.itis.bloom.shared.core.data.network

interface AppLogger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String)
}

expect fun createAppLogger(): AppLogger