package io.simple.model

data class HttpMethodAndRoute(
    val method: HttpMethod,
    val route: String
) {

    companion object {
        fun parse(line: String) : HttpMethodAndRoute {
            val parts = line.split(' ')
            val method = HttpMethod.parse(parts[0])
            val route = parts[1]

            return HttpMethodAndRoute(method, route)
        }
    }
}