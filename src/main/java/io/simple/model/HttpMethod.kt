package io.simple.model;

import io.simple.exception.HttpException

enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    companion object {
        fun parse(value: String): HttpMethod {
            return HttpMethod.entries.find { it.name == value } ?: throw HttpException.UnknownHttpMethodException(value)
        }
    }
}