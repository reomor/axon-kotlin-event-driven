package com.github.reomor.productservice.query.rest.dto

import java.math.BigDecimal

class ProductDto(
  val productId: String,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
)
