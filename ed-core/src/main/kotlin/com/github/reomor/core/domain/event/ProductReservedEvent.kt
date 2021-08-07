package com.github.reomor.core.domain.event

data class ProductReservedEvent(
  val productId: String,
  val quantity: Long,
  val orderId: String,
  val userId: String
)
