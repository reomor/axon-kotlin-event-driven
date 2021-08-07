package com.github.reomor.core.domain.event

data class ProductReservationCancelEvent(
  val productId: String,
  val quantity: Long,
  val orderId: String,
  val userId: String,
  val reason: String
)
