package io.simple.server

import io.simple.model.HttpMethod
import io.simple.model.HttpReceivedRoute
import io.simple.model.HttpRequest

interface RouteScope {
    val request: HttpRequest
    val route: HttpReceivedRoute get() = request.methodAndRoute.route
    val queryParams: Map<String, String> get() = request.methodAndRoute.route.queryParams
    val pathParams: Map<String, String> get() = request.methodAndRoute.route.pathParams
    val method: HttpMethod get() = request.methodAndRoute.method
}