package io.simple.exception

import io.simple.model.HttpMethodAndRoute

sealed class HttpException (message: String) : Exception(message) {

    class NoRouteHandlerFoundException(methodAndRoute: HttpMethodAndRoute): HttpException("No route handler found for ${methodAndRoute.route} ${methodAndRoute.route}")
    class HeaderNotFoundException (header: String) : HttpException("No $header is present in request")
    class UnknownHttpMethodException (method: String) : HttpException("Unknown HTTP method $method")
    class UnknownContentTypeException (type: String) : HttpException("Unknown Content-Type: $type")
    class UnsupportedStatusCodeException (code: Int) : HttpException("Unsupported status code: $code")
}