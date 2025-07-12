package io.simple.plugin.defaults

import io.simple.model.ContentTypeValues
import io.simple.model.HttpHeaders
import io.simple.model.HttpResponse
import io.simple.plugin.send.HttpSendPlugin
import io.simple.plugin.send.HttpSendPluginScope
import io.simple.utils.findHeader

val ContentTypePlugin = object: HttpSendPlugin<HttpContentTypeSendPluginScope> {
    override val name: String = "ContentTypePlugin"
    override val scopeImpl: HttpSendPluginScope.() -> HttpContentTypeSendPluginScope = {
        val httpSendPluginScope = this
        object : HttpContentTypeSendPluginScope {
            override val response: HttpResponse = httpSendPluginScope.response
            override fun overrideResponse(value: HttpResponse) { httpSendPluginScope.overrideResponse(value) }

            override fun setDefaultContentType(type: ContentTypeValues) {
                val headers = response.headers
                if (headers.findHeader<HttpHeaders.ContentType>() != null) return

                overrideResponse(
                    value = response.copy(
                        headers = response.headers + HttpHeaders.ContentType(ContentTypeValues.JSON)
                    )
                )
            }
        }
    }
}

interface HttpContentTypeSendPluginScope : HttpSendPluginScope {
    fun setDefaultContentType(type: ContentTypeValues)
}