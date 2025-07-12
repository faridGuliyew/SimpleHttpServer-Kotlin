package io.simple.plugin.send

interface HttpSendPlugin <T : HttpSendPluginScope> {
    val name: String

    val scopeImpl: HttpSendPluginScope.() -> T
}
