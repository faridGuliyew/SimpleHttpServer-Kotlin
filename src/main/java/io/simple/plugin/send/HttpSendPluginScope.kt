package io.simple.plugin.send

import io.simple.model.HttpResponse

interface HttpSendPluginScope {
    val response: HttpResponse

    fun overrideResponse(value: HttpResponse = response)
}