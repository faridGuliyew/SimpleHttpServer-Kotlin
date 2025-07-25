package io.simple.utils

import io.simple.logging.Logger
import io.simple.logging.LoggerLevel
import io.simple.model.HttpMethod
import io.simple.model.HttpRouteHandler

fun MutableMap<HttpMethod, MutableMap<String, HttpRouteHandler>>.addHandler(
    method: HttpMethod,
    handler: HttpRouteHandler,
    logger: Logger
) {
    val isHandlerAlreadyRegisteredForRoute = this[method]?.containsKey(handler.definition.route) == true
    if (isHandlerAlreadyRegisteredForRoute) {
        logger.log("There are multiple handlers for $method ${handler.definition}. Overriding...", LoggerLevel.WARN)
    }

    this[method] = this[method].orEmpty().toMutableMap().apply {
        put(handler.definition.route, handler)
    }
}