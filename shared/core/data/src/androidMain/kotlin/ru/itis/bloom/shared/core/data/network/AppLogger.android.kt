package ru.itis.bloom.shared.core.data.network

import android.util.Log

actual fun createAppLogger(): AppLogger = object : AppLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}