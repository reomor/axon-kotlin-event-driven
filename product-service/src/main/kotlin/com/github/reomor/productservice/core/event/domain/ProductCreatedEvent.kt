package com.github.reomor.productservice.core.event.domain

import com.github.reomor.productservice.command.event.command.ProductId
import java.math.BigDecimal

data class ProductCreatedEvent(
  val id: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
)
