package com.github.reomor.productservice.core.error.handler

import com.github.reomor.productservice.core.error.RestError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ProductServiceErrorHandler {

  @ExceptionHandler(
    value = [
      IllegalStateException::class
    ]
  )
  fun handleIllegalStateException(
    e: IllegalStateException,
    request: WebRequest
  ): ResponseEntity<RestError> {
    return ResponseEntity(
      RestError(
        errorMessage = e.message
      ),
      HttpHeaders(),
      HttpStatus.INTERNAL_SERVER_ERROR
    )
  }

  @ExceptionHandler(
    value = [
      Exception::class
    ]
  )
  fun defaultExceptionHandler(
    e: IllegalStateException,
    request: WebRequest
  ): ResponseEntity<RestError> {
    return ResponseEntity(
      RestError(
        errorMessage = e.message
      ),
      HttpHeaders(),
      HttpStatus.INTERNAL_SERVER_ERROR
    )
  }
}
