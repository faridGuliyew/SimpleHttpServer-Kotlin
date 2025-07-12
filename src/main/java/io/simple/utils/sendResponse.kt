package io.simple.utils

import io.simple.model.HttpResponse
import java.net.Socket

fun Socket.sendResponseAndClose(response: HttpResponse) {
    // Send the response over & close connection
    val writer = getOutputStream().bufferedWriter()
    writer.write(response.convertToString())
    writer.flush()
    close()
}