package com.github.reomor.productservice.core.domain.event

import com.github.reomor.productservice.core.ProductId
import java.math.BigDecimal

data class ProductCreatedEvent(
  val productId: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Long
)
