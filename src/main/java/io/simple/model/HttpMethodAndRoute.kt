package io.simple.model

data class HttpMethodAndRoute(
    val method: HttpMethod,
    val route: HttpReceivedRoute
) {

    companion object {
        fun parse(line: String) : HttpMethodAndRoute {
            val parts = line.split(' ')
            val method = HttpMethod.parse(parts[0])
            val route = HttpReceivedRoute.parse(parts[1])

            return HttpMethodAndRoute(method, route)
        }
    }
}

data class HttpRouteDefinition(
    val route: String,
    val pathParams: List<String> = emptyList()
)

data class HttpReceivedRoute(
    val route: String,
    val queryParams: Map<String, String>,
    val pathParams: Map<String, String>
) {
    companion object {
        fun parse(line: String): HttpReceivedRoute { // -> /echo/2/1?q=hi&name=farid
            val route = line.substringBefore('?')
            val querySection = line.substringAfter('?', "")
            val queries = querySection.split('&').takeIf { it.all { it.isNotEmpty() } }.orEmpty()
            val queryMap = queries.map {
                val keyAndValue = it.split('=', limit = 2)
                keyAndValue[0] to keyAndValue[1]
            }.associateBy (keySelector = { it.first }, valueTransform = { it.second })

            return HttpReceivedRoute(
                route = route,
                queryParams = queryMap,
                pathParams = emptyMap()
            )
        }
    }
}