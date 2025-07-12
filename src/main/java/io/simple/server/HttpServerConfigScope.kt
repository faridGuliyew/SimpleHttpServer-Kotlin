package io.simple.server

import io.simple.model.HttpResponse

interface HttpServerConfigScope {


    fun get(route: String, block: RouteScope.() -> HttpResponse)

    fun post(route: String, block: RouteScope.() -> HttpResponse)

    fun put(route: String, block: RouteScope.() -> HttpResponse)

    fun patch(route: String, block: RouteScope.() -> HttpResponse)

    fun delete(route: String, block: RouteScope.() -> HttpResponse)

}