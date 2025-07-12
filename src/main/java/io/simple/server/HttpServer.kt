package io.simple.server

import io.simple.exception.HttpException
import io.simple.logging.Logger
import io.simple.logging.LoggerLevel
import io.simple.logging.errorWithLog
import io.simple.model.*
import io.simple.utils.addHandler
import io.simple.utils.findHeader
import io.simple.utils.sendResponseAndClose
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread


class HttpServer (
    val port: Int,
    loggerLevel: LoggerLevel = LoggerLevel.ALL,
    val config: HttpServerConfigScope.() -> Unit
) {
    private val logger = Logger(loggerLevel)
    private val serverSocket = ServerSocket(port)
    private val handlers = mutableMapOf<HttpMethod, MutableMap<String, HttpRouteHandler>>()

    init {
        configureServer()
        startListening()
    }

    private fun handleClient(clientSocket: Socket) {
        logger.log("Accepted client: $clientSocket", LoggerLevel.INFO)

        val reader = clientSocket.getInputStream().bufferedReader()

        val requestFirstLine = reader.readLine()
        val httpMethodAndRoute = HttpMethodAndRoute.parse(requestFirstLine)
        logger.log("${httpMethodAndRoute.method} ${httpMethodAndRoute.route}", LoggerLevel.INFO)
        // Locate a registered handler for the route
        val handlersForMethod = handlers[httpMethodAndRoute.method] ?: run {
            logger.errorWithLog(HttpException.NoRouteHandlerFoundException(httpMethodAndRoute))
        }
        val handler = handlersForMethod[httpMethodAndRoute.route] ?: run {
            logger.errorWithLog(HttpException.NoRouteHandlerFoundException(httpMethodAndRoute))
        }

        val requestHeaders = mutableListOf<HttpHeaders>()
        while (true) {
            val line = reader.readLine() ?: break
            if (line.isEmpty()) break
            requestHeaders.add(HttpHeaders.parse(line))
        }

        val contentLength = requestHeaders.findHeader<HttpHeaders.ContentLength>() ?: run {
            logger.errorWithLog(HttpException.HeaderNotFoundException(HttpHeaders.CONTENT_LENGTH))
        }

        val bodyChars = CharArray(contentLength.length)
        reader.read(bodyChars)
        val body = String(bodyChars)

        // Build request
        val httpRequest = HttpRequest(
            headers = requestHeaders,
            body = body
        )

        // Get response
        val routeScope = object : RouteScope { override val request: HttpRequest = httpRequest }
        val httpResponse = handler.block(routeScope)
        clientSocket.sendResponseAndClose(httpResponse)
    }


    private fun startListening() {
        logger.log("Listening for clients on 0.0.0.0:$port", LoggerLevel.INFO)
        while (true) {
            val clientSocket = serverSocket.accept()

            thread {
                try {
                    handleClient(clientSocket)
                } catch (e: Exception) {
                    clientSocket.sendResponseAndClose(HttpResponse.noHandlerFound)
                }
            }
        }
    }

    private fun configureServer() {
        logger.log("Configuring the server...", LoggerLevel.INFO)
        val httpServerConfigScope = object : HttpServerConfigScope {
            override fun get(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.GET, handler, logger)
            }

            override fun post(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.POST, handler, logger)
            }

            override fun put(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.PUT, handler, logger)
            }

            override fun patch(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.PATCH, handler, logger)
            }

            override fun delete(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.DELETE, handler, logger)
            }
        }
        config(httpServerConfigScope)
        logger.log("Configuration is done!", LoggerLevel.INFO)
    }
}