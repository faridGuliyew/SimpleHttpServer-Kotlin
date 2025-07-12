# 🔥 Simple Kotlin HTTP Server

A lightweight and customizable Kotlin HTTP server with support for all major HTTP methods, custom headers, and content-type management.

---

## 🚀 Features

- ✅ Supports **GET**, **POST**, **PUT**, **PATCH**, **DELETE**
- 🔐 Custom header support
- 📦 Custom plugin support
- 🧾 Easy request logging
- ✨ Simple and readable DSL

---

## 🛠️ Example Usage

Here’s how to create a server that echoes requests on `/echo` for all HTTP methods:

```kotlin
HttpServer(
    port = 65000,
    loggerLevel = LoggerLevel.ALL
) {

    // Install content-type plugin with default value
    install(ContentTypePlugin) {
        setDefaultContentType(ContentTypeValues.JSON)
    }

    // POST /echo
    post("/echo") {
        return@post HttpResponse(
            statusCode = HttpStatusCode.OK,
            headers = listOf(
                HttpHeaders.CustomHeaders("Authorization", "Bearer 123")
            ),
            body = request.body
        )
    }

    // PUT /echo
    put("/echo") {
        HttpResponse(
            statusCode = HttpStatusCode.OK,
            headers = listOf(
                HttpHeaders.ContentType(ContentTypeValues.JSON),
                HttpHeaders.CustomHeaders("Authorization", "Bearer 123")
            ),
            body = request.body
        )
    }

    // PATCH /echo
    patch("/echo") {
        HttpResponse(
            statusCode = HttpStatusCode.OK,
            body = request.body
        )
    }

    // DELETE /echo
    delete("/echo") {
        HttpResponse(
            statusCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
            headers = listOf(
                HttpHeaders.ContentType(ContentTypeValues.JSON),
                HttpHeaders.CustomHeaders("Authorization", "Bearer 123")
            ),
            body = request.body
        )
    }

    // GET /echo
    get("/echo") {
        HttpResponse(
            statusCode = HttpStatusCode.OK,
            headers = listOf(
                HttpHeaders.ContentType(ContentTypeValues.JSON),
                HttpHeaders.CustomHeaders("Authorization", "Bearer 123")
            ),
            body = request.body
        )
    }
}
```


## 📬 Try it out

```curl
curl -X POST http://localhost:65000/echo \
  -H "Content-Type: application/json" \
  -d '{"message": "hello"}'
```

## 🧪 Coming soon

Middleware support
Route/Query parameters
Library publishing
