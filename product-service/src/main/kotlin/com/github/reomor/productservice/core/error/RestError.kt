package com.github.reomor.productservice.core.error

import java.time.LocalDateTime

data class RestError(
  val timestamp: LocalDateTime = LocalDateTime.now(),
  val errorStatus: Int = 400,
  val errorMessage: String? = ""
)
