package io.simple.server

import io.simple.logging.LoggerLevel
import io.simple.model.HttpRouteDefinition
import io.simple.model.HttpResponse
import io.simple.plugin.receive.HttpReceivePlugin
import io.simple.plugin.receive.HttpReceivePluginScope
import io.simple.plugin.send.HttpSendPlugin
import io.simple.plugin.send.HttpSendPluginScope

interface HttpServerConfigScope {

    fun log(message: String, level: LoggerLevel = LoggerLevel.DEBUG)

    fun get(route: String, block: RouteScope.() -> HttpResponse)

    fun get(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse)

    fun post(route: String, block: RouteScope.() -> HttpResponse)

    fun post(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse)

    fun put(route: String, block: RouteScope.() -> HttpResponse)

    fun put(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse)

    fun patch(route: String, block: RouteScope.() -> HttpResponse)

    fun patch(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse)

    fun delete(route: String, block: RouteScope.() -> HttpResponse)

    fun delete(route: HttpRouteDefinition, block: RouteScope.() -> HttpResponse)

    fun <T : HttpSendPluginScope> install(plugin: HttpSendPlugin<T>, block: T.() -> Unit)

    fun <T : HttpReceivePluginScope> install(plugin: HttpReceivePlugin<T>, block: T.() -> Unit)
}
