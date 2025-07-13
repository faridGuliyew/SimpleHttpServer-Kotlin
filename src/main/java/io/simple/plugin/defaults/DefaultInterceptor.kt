package io.simple.plugin.defaults

import io.simple.model.HttpRequest
import io.simple.model.HttpResponse
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPlugin
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPluginScope

val DefaultInterceptorPlugin = object : HttpReceiveAndSendPlugin<DefaultInterceptorPluginScope> {
    override val name: String
        get() = "DefaultInterceptorPlugin"
    override val scopeImpl: HttpReceiveAndSendPluginScope.() -> DefaultInterceptorPluginScope = {
        val receiveAndSendPluginScope = this
        object : DefaultInterceptorPluginScope{
            override fun intercept(block: (request: HttpRequest, response: HttpResponse) -> HttpResponse) {
                val response = block(request, response)
                overrideResponse(response)
            }

            override val request: HttpRequest = receiveAndSendPluginScope.request
            override val response: HttpResponse = receiveAndSendPluginScope.response
            override fun overrideRequest(value: HttpRequest) {
                receiveAndSendPluginScope.overrideRequest(value)
            }
            override fun overrideResponse(value: HttpResponse) {
                receiveAndSendPluginScope.overrideResponse(value)
            }
        }
    }
}

interface DefaultInterceptorPluginScope : HttpReceiveAndSendPluginScope {
    fun intercept(block: (request: HttpRequest, response: HttpResponse) -> HttpResponse)
}