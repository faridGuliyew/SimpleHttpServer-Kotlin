package io.simple.model

import io.simple.exception.HttpException

enum class HttpStatusCode (val code: Int, val message: String) {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");


    companion object {
        fun parse(code: Int): HttpStatusCode {
            return entries.find { it.code == code } ?: throw HttpException.UnsupportedStatusCodeException(code)
        }
    }
}