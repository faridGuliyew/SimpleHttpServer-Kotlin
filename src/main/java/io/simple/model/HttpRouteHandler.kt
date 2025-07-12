package io.simple.model

import io.simple.server.RouteScope


data class HttpRouteHandler (
    val route: String,
    val block: RouteScope.() -> HttpResponse
)