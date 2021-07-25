package com.github.reomor.productservice.rest.dto

import java.math.BigDecimal

data class CreateProductRequest(
  val name: String,
  val price: BigDecimal,
  val quantity: Int
)
