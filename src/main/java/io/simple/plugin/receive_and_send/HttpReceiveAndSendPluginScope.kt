package io.simple.plugin.receive_and_send

import io.simple.model.HttpRequest
import io.simple.model.HttpResponse

interface HttpReceiveAndSendPluginScope {
    val request: HttpRequest
    val response: HttpResponse

    fun overrideRequest(value: HttpRequest = request)
    fun overrideResponse(value: HttpResponse = response)
}