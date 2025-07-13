package io.simple.plugin.receive_and_send

/** HttpReceiveAndSendPlugin is applied after HttpReceivePlugin & HttpSendPlugin.
 * Meaning, it will override their behaviour
 * */
interface HttpReceiveAndSendPlugin <T : HttpReceiveAndSendPluginScope> {
    val name: String

    val scopeImpl: HttpReceiveAndSendPluginScope.() -> T
}