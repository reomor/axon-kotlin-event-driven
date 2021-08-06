package com.github.reomor.core.event.domain

data class ProductReservedEvent(
  val productId: String,
  val quantity: Long,
  val orderId: String,
  val userId: String
)
