package io.simple.model

class HttpResponse (
    val statusCode: HttpStatusCode,
    val headers: List<HttpHeaders>,
    val body: String
) {

    fun convertToString() : String {
        val contentLength = HttpHeaders.ContentLength(body.length)
        val headerLines = (headers + contentLength).joinToString("\n") { header ->
            "${header.key}: ${header.value}"
        }

        val requestFirstLine = "HTTP/1.1 ${statusCode.code} ${statusCode.message}"
        return "$requestFirstLine\n$headerLines\n\n$body"
    }

    companion object {
        val noHandlerFound = HttpResponse(
            statusCode = HttpStatusCode.NOT_FOUND,
            headers = emptyList(),
            body = ""
        )
    }
}