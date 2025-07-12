package io.simple.plugin.defaults

import io.simple.plugin.receive.HttpReceivePlugin
import io.simple.plugin.receive.HttpReceivePluginScope

val DefaultReceivePlugin = object : HttpReceivePlugin<HttpReceivePluginScope> {
    override val name: String
        get() = "DefaultReceivePlugin"

    override val scopeImpl: HttpReceivePluginScope.() -> HttpReceivePluginScope = { this }
}