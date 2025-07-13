package io.simple

import io.simple.logging.LoggerLevel
import io.simple.model.*
import io.simple.plugin.defaults.ContentTypePlugin
import io.simple.plugin.defaults.DefaultInterceptorPlugin
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

        install(DefaultInterceptorPlugin) {
            intercept { request, response->

                if (request.body.isEmpty()) return@intercept response.copy(
                    body = "BODY SHOULD NOT BE EMPTY!"
                )

                return@intercept response
            }
        }

        post(
            route = HttpRouteDefinition(
                route = "/echo",
                pathParams = listOf("command", "id")
            )
        ) {
            val name = queryParams["name"]
            val age = queryParams["age"]
            val command = pathParams["command"]
            val id = pathParams["id"]

            return@post HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = "Command: $command. Id: $id. name: $name, age: $age"
            )
        }

        post(
            route = HttpRouteDefinition(
                route = "/echo/command",
                pathParams = listOf("id")
            )
        ) {
            val name = queryParams["name"]
            val age = queryParams["age"]
            val id = pathParams["id"]

            return@post HttpResponse(
                statusCode = HttpStatusCode.OK,
                headers = listOf(HttpHeaders.CustomHeaders("Authorization","Bearer 123")),
                body = "Id: $id. name: $name, age: $age"
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

