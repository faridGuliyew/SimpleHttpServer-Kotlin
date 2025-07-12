package io.simple.plugin.defaults

import io.simple.plugin.send.HttpSendPlugin
import io.simple.plugin.send.HttpSendPluginScope

val DefaultSendPlugin = object : HttpSendPlugin<HttpSendPluginScope> {
    override val name: String
        get() = "DefaultSendPlugin"
    override val scopeImpl: HttpSendPluginScope.() -> HttpSendPluginScope = { this }
}