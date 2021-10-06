package net.krupizde.mediaApp.presentation.controllers

import org.springframework.http.ResponseEntity

fun <T> createListResponse(list: List<T>?, page: Int?, size: Int): ResponseEntity<List<T>> {
    var output = list
    if (page != null)
        output = output?.subList(page * size, page * size + size)
    return when {
        output == null -> ResponseEntity.notFound().build()
        output.isEmpty() -> ResponseEntity.noContent().build()
        else -> ResponseEntity.ok(output)
    }
}

fun <T> createResponseWithCall(call: () -> T): ResponseEntity<Any> {
    return try {
        ResponseEntity.ok(call())
    } catch (e: IllegalArgumentException) {
        ResponseEntity.badRequest().body(e.localizedMessage)
    } catch (e: IllegalStateException) {
        ResponseEntity.badRequest().body(e.localizedMessage)
    } catch (e: Exception) {
        ResponseEntity.internalServerError().body(e.localizedMessage)
    }
}