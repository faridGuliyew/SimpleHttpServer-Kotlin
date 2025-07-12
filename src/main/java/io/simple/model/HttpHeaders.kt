package io.simple.model

sealed interface HttpHeaders {

    val key: String
    val value: String

    data class ContentType (val type: ContentTypeValues) : HttpHeaders {
        override val key: String = CONTENT_TYPE
        override val value: String = type.value
    }

    data class ContentLength (val length: Int) : HttpHeaders {
        override val key: String = CONTENT_LENGTH
        override val value: String = "$length"
    }

    data class CustomHeaders (val headerKey: String, val headerValue: String) : HttpHeaders {
        override val key: String = headerKey
        override val value: String = headerValue
    }


    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_LENGTH = "Content-Length"
        fun parse(line: String) : HttpHeaders {
            val key = line.substringBefore(':').trim()
            val value = line.substringAfter(':').trim()
            return when (key) {
                CONTENT_TYPE -> ContentType(ContentTypeValues.parse(value))
                CONTENT_LENGTH -> ContentLength(value.toInt())
                else -> CustomHeaders(key, value)
            }
        }
    }
}