package io.simple.plugin.receive

import io.simple.model.HttpRequest

interface HttpReceivePluginScope {
    val request: HttpRequest

    fun overrideRequest(value: HttpRequest = request)
}