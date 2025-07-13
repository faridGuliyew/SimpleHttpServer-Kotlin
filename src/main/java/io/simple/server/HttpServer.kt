package io.simple.server

import io.simple.exception.HttpException
import io.simple.logging.Logger
import io.simple.logging.LoggerLevel
import io.simple.logging.errorWithLog
import io.simple.model.*
import io.simple.model.HttpHeaders.Companion.CONTENT_LENGTH
import io.simple.plugin.receive.HttpReceivePlugin
import io.simple.plugin.receive.HttpReceivePluginScope
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPlugin
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPluginScope
import io.simple.plugin.send.HttpSendPlugin
import io.simple.plugin.send.HttpSendPluginScope
import io.simple.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val receivePlugins = mutableReceivePluginMapOf<HttpReceivePluginScope>()
    private val sendPlugins = mutableSendPluginMapOf<HttpSendPluginScope>()
    private val receiveAndSendPlugins = mutableReceiveAndSendPluginMapOf<HttpReceiveAndSendPluginScope>()

    init {
        configureServer()
        startListening()
    }

    private fun handleClient(clientSocket: Socket) {
        logger.log("Accepted client: $clientSocket", LoggerLevel.INFO)

        val reader = clientSocket.getInputStream().bufferedReader()

        val requestFirstLine = reader.readLine()
        var httpMethodAndRoute = HttpMethodAndRoute.parse(requestFirstLine)
        logger.log("RECEIVED -> ${httpMethodAndRoute.method} ${httpMethodAndRoute.route}", LoggerLevel.INFO)
        // Locate a registered route definition for the requested route.
        val handlersForMethod = handlers[httpMethodAndRoute.method] ?: run {
            logger.errorWithLog(HttpException.NoRouteHandlerFoundException(httpMethodAndRoute))
        }
        val (correspondingRoute, correspondingDefinition) = handlersForMethod.findMatchingReceivedRouteAndDefinition(httpMethodAndRoute.route) ?: run {
            logger.errorWithLog(HttpException.NoRouteHandlerFoundException(httpMethodAndRoute))
        }
        val handler = handlersForMethod[correspondingDefinition.route] ?: run {
            logger.errorWithLog(HttpException.NoRouteHandlerFoundException(httpMethodAndRoute))
        }
        // Match received route to definition - resolve keys
        httpMethodAndRoute = httpMethodAndRoute
            .copy(route = correspondingRoute.resolvePathParamKeysByRouteDefinition(correspondingDefinition))

        logger.log("HANDLED AS -> ${httpMethodAndRoute.method} ${httpMethodAndRoute.route}", LoggerLevel.INFO)

        // Parse all headers
        val requestHeaders = mutableListOf<HttpHeaders>()
        while (true) {
            val line = reader.readLine() ?: break
            if (line.isEmpty()) break
            requestHeaders.add(HttpHeaders.parse(line))
        }

        val contentLength = requestHeaders.findHeader<HttpHeaders.ContentLength>()
        if (contentLength == null) {
            logger.log(HttpException.HeaderNotFoundException(CONTENT_LENGTH))
        }

        // Parse body
        val bodyChars = CharArray(contentLength?.length ?: 0)
        reader.read(bodyChars)
        val body = String(bodyChars)

        // Build request
        var httpRequest = HttpRequest(
            methodAndRoute = httpMethodAndRoute,
            headers = requestHeaders,
            body = body
        )
        // Apply receive plugins
        val receivePluginScope = object : HttpReceivePluginScope {
            override val request: HttpRequest = httpRequest
            override fun overrideRequest(value: HttpRequest) { httpRequest = value }
        }
        receivePlugins.forEach { (plugin, block) -> block.invoke(plugin.scopeImpl(receivePluginScope)) }

        // Generate response
        val routeScope = object : RouteScope { override val request: HttpRequest = httpRequest }
        var httpResponse = handler.block(routeScope)
        // Apply send plugins
        val sendPluginScope = object : HttpSendPluginScope {
            override val response: HttpResponse = httpResponse
            override fun overrideResponse(value: HttpResponse) { httpResponse = value }
        }
        sendPlugins.forEach { plugin, block-> block.invoke(plugin.scopeImpl(sendPluginScope)) }

        // Apply receive & send plugins
        val receiveAndSendPluginScope = object : HttpReceiveAndSendPluginScope {
            override val request: HttpRequest = httpRequest
            override val response: HttpResponse = httpResponse
            override fun overrideRequest(value: HttpRequest) { httpRequest = value }
            override fun overrideResponse(value: HttpResponse) { httpResponse = value }
        }
        receiveAndSendPlugins.forEach {
            (plugin, block) -> block.invoke(plugin.scopeImpl(receiveAndSendPluginScope))
        }

        clientSocket.sendResponseAndClose(httpResponse)
    }


    private fun startListening() {
        logger.log("Listening for clients on 0.0.0.0:$port", LoggerLevel.INFO)
        val scope = CoroutineScope(Dispatchers.IO)
        while (true) {
            val clientSocket = serverSocket.accept()
            scope.launch {
                try {
                    handleClient(clientSocket)
                } catch (e: Exception) {
                    logger.log(e.message.orEmpty(), LoggerLevel.ERROR)
                    clientSocket.sendResponseAndClose(HttpResponse.noHandlerFound)
                }
            }
        }
    }

    private fun configureServer() {
        logger.log("Configuring the server...", LoggerLevel.INFO)
        val httpServerConfigScope = object : HttpServerConfigScope {
            override fun log(message: String, level: LoggerLevel) {
                logger.log(message, level)
            }

            override fun get(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(HttpRouteDefinition(route), block)
                handlers.addHandler(HttpMethod.GET, handler, logger)
            }

            override fun get(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.GET, handler, logger)
            }

            override fun post(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(HttpRouteDefinition(route), block)
                handlers.addHandler(HttpMethod.POST, handler, logger)
            }

            override fun post(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.POST, handler, logger)
            }

            override fun put(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(HttpRouteDefinition(route), block)
                handlers.addHandler(HttpMethod.PUT, handler, logger)
            }
            override fun put(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.PUT, handler, logger)
            }

            override fun patch(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(HttpRouteDefinition(route), block)
                handlers.addHandler(HttpMethod.PATCH, handler, logger)
            }
            override fun patch(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.PATCH, handler, logger)
            }

            override fun delete(route: String, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(HttpRouteDefinition(route), block)
                handlers.addHandler(HttpMethod.DELETE, handler, logger)
            }

            override fun delete(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse) {
                val handler = HttpRouteHandler(route, block)
                handlers.addHandler(HttpMethod.DELETE, handler, logger)
            }

            override fun <T : HttpSendPluginScope> install(plugin: HttpSendPlugin<T>, block: T.() -> Unit) {
                logger.log("Installed send plugin: ${plugin.name}", LoggerLevel.INFO)
                sendPlugins[plugin] = block as HttpSendPluginScope.() -> Unit
            }

            override fun <T : HttpReceivePluginScope> install(plugin: HttpReceivePlugin<T>, block: T.() -> Unit) {
                logger.log("Installed receive plugin: ${plugin.name}", LoggerLevel.INFO)
                receivePlugins[plugin] = block as HttpReceivePluginScope.() -> Unit
            }

            override fun <T : HttpReceiveAndSendPluginScope> install(
                plugin: HttpReceiveAndSendPlugin<T>,
                block: T.() -> Unit
            ) {
                logger.log("Installed receive and send plugin: ${plugin.name}", LoggerLevel.INFO)
                receiveAndSendPlugins[plugin] = block as HttpReceiveAndSendPluginScope.() -> Unit
            }
        }
        config(httpServerConfigScope)
        logger.log("Configuration is done!", LoggerLevel.INFO)
    }
}