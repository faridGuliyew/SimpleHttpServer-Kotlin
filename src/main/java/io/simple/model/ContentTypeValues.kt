package io.simple.model;

import io.simple.exception.HttpException

enum class ContentTypeValues (val value: String) {
    JSON("application/json"), TEXT_PLAIN("text/plain");

    companion object {
        fun parse(value: String): ContentTypeValues {
            return entries.find { it.value == value } ?: throw HttpException.UnknownContentTypeException(value)
        }
    }
}