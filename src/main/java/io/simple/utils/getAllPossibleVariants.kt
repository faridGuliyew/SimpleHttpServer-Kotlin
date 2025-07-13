package io.simple.utils

import io.simple.model.HttpReceivedRoute

fun HttpReceivedRoute.getAllPossibleVariants(): List<HttpReceivedRoute> {
    val possiblePathParams = route.split('/')

    val possibleVariants = mutableListOf<HttpReceivedRoute>()

    for (i in possiblePathParams.size downTo 1) {
        var route = possiblePathParams.subList(0, i).joinToString("/")
        if (route == "") route = "/"
        val pathParameters = possiblePathParams.subList(i, possiblePathParams.size)
        possibleVariants.add(
            copy(
                route = route,
                pathParams = pathParameters.associateBy(keySelector = { it }, valueTransform = { it })
            )
        )
    }

    return possibleVariants
}