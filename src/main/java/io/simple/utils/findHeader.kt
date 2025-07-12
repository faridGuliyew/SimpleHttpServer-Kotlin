package io.simple.utils

import io.simple.model.HttpHeaders

inline fun <reified T: HttpHeaders> List<HttpHeaders>.findHeader() : T? {
    return find { it is T } as? T
}