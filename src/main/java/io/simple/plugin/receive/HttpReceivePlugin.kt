package io.simple.plugin.receive

interface HttpReceivePlugin <T : HttpReceivePluginScope> {
    val name: String

    val scopeImpl: HttpReceivePluginScope.() -> T
}

