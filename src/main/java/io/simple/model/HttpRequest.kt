package io.simple.model

data class HttpRequest (
    val methodAndRoute: HttpMethodAndRoute,
    val headers: List<HttpHeaders>,
    val body: String
)