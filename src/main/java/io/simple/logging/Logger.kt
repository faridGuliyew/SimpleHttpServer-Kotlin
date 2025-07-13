package io.simple.logging

import io.simple.exception.HttpException

class Logger (private val loggerLevel: LoggerLevel) {

    fun log(message: String, level: LoggerLevel) {
        if (loggerLevel == LoggerLevel.NONE) return
        if (level != loggerLevel && loggerLevel != LoggerLevel.ALL) return

        logUnchecked(message, level)
    }

    fun log(exception: HttpException) {
        val level = LoggerLevel.WARN
        if (loggerLevel == LoggerLevel.NONE) return
        if (level != loggerLevel && loggerLevel != LoggerLevel.ALL) return

        logUnchecked(exception.message.orEmpty(), level)
    }

    private fun logUnchecked(message: String, level: LoggerLevel) {
        println("$level - $message")
    }
}

inline fun Logger.errorWithLog(exception: HttpException): Nothing {
    log(exception.message.orEmpty(), LoggerLevel.ERROR)
    throw exception
}

enum class LoggerLevel {
    ALL, NONE, WARN, ERROR, INFO, DEBUG
}