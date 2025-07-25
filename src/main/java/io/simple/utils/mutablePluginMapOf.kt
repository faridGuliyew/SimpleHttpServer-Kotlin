package io.simple.utils

import io.simple.plugin.receive.HttpReceivePlugin
import io.simple.plugin.receive.HttpReceivePluginScope
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPlugin
import io.simple.plugin.receive_and_send.HttpReceiveAndSendPluginScope
import io.simple.plugin.send.HttpSendPlugin
import io.simple.plugin.send.HttpSendPluginScope

fun <T : HttpSendPluginScope> mutableSendPluginMapOf() = mutableMapOf<HttpSendPlugin<*>, T.() -> Unit>()

fun <T : HttpReceivePluginScope> mutableReceivePluginMapOf() = mutableMapOf<HttpReceivePlugin<*>, T.() -> Unit>()

fun <T : HttpReceiveAndSendPluginScope> mutableReceiveAndSendPluginMapOf() = mutableMapOf<HttpReceiveAndSendPlugin<*>, T.() -> Unit>()