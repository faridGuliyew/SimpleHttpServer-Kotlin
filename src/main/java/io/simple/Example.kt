package io.simple

import io.simple.server.HttpServer
import io.simple.model.ContentTypeValues
import io.simple.model.HttpHeaders
import io.simple.model.HttpResponse
import io.simple.model.HttpStatusCode

fun main() {
    HttpServer(65000) {
        post("/echo") {
            return@post HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.ContentType(ContentTypeValues.JSON), HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = request.body
            )
        }

        put("/echo") {
             HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.ContentType(ContentTypeValues.JSON), HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = request.body
            )
        }

        patch("/echo") {
             HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.ContentType(ContentTypeValues.JSON), HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = request.body
            )
        }

        delete("/echo") {
             HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.ContentType(ContentTypeValues.JSON), HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = request.body
            )
        }

        get("/echo") {
             HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.ContentType(ContentTypeValues.JSON), HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = request.body
            )
        }
    }
}