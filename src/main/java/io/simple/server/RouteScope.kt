package io.simple.server

import io.simple.model.HttpRequest

interface RouteScope {
    val request: HttpRequest
}