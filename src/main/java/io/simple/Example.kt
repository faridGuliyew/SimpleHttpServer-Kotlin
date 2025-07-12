package io.simple

import io.simple.logging.LoggerLevel
import io.simple.model.ContentTypeValues
import io.simple.model.HttpHeaders
import io.simple.model.HttpResponse
import io.simple.model.HttpStatusCode
import io.simple.plugin.defaults.ContentTypePlugin
import io.simple.server.*
import io.simple.utils.findHeader

fun main() {
    HttpServer(
        port = 65000,
        loggerLevel = LoggerLevel.ALL
    ) {

        install(ContentTypePlugin) {
            setDefaultContentType(ContentTypeValues.JSON)
        }

        post("/echo") {
            return@post HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
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
                body = request.body
            )
        }

        delete("/echo") {
             HttpResponse(
                statusCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
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

