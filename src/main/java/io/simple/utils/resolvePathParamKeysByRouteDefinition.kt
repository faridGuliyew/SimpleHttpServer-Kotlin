package io.simple.utils

import io.simple.model.HttpReceivedRoute
import io.simple.model.HttpRouteDefinition

fun HttpReceivedRoute.resolvePathParamKeysByRouteDefinition(definition: HttpRouteDefinition): HttpReceivedRoute {
    val pathKeys = definition.pathParams
    val pathValues = this.pathParams.values.toList()

    val pathParams = mutableMapOf<String, String>()
    for (i in pathKeys.indices) {
        pathKeys[i]
        pathValues[i]
        pathParams[pathKeys[i]] = pathValues[i]
    }

    return this.copy(
        pathParams = pathParams
    )
}