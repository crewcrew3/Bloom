package ru.itis.bloom.shared.core.data.network

actual fun createAppLogger(): AppLogger = object : AppLogger {
    override fun d(tag: String, message: String) {
        println("[$tag] DEBUG: $message")
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        System.err.println("[$tag] ERROR: $message")
        throwable?.printStackTrace()
    }

    override fun i(tag: String, message: String) {
        println("[$tag] INFO: $message")
    }
}