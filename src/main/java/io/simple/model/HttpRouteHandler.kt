package io.simple.model

import io.simple.server.RouteScope


data class HttpRouteHandler (
    val definition: HttpRouteDefinition,
    val block: RouteScope.() -> HttpResponse
)