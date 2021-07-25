package com.github.reomor.productservice.events.domain

import com.github.reomor.productservice.events.command.ProductId
import java.math.BigDecimal

data class ProductCreatedEvent(
  val id: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
)
