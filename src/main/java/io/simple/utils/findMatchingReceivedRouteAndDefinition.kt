package io.simple.utils

import io.simple.model.HttpReceivedRoute
import io.simple.model.HttpRouteDefinition
import io.simple.model.HttpRouteHandler

fun MutableMap<String, HttpRouteHandler>.findMatchingReceivedRouteAndDefinition(
    receivedRoute: HttpReceivedRoute
) : Pair<HttpReceivedRoute, HttpRouteDefinition>? {
    val acceptedRouteVariant = receivedRoute.getAllPossibleVariants().firstOrNull {
        this.containsKey(it.route)
    } ?: return null

    val definition = this[acceptedRouteVariant.route]?.definition ?: return null
    // Check if definition supports given path params
    val pathParamsSupported = definition.pathParams.size == acceptedRouteVariant.pathParams.size
    if (!pathParamsSupported) return null

    return acceptedRouteVariant to definition
}