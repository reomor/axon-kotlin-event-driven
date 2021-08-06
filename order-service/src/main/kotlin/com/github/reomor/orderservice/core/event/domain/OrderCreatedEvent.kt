package com.github.reomor.orderservice.core.event.domain

import com.github.reomor.orderservice.core.*

data class OrderCreatedEvent(
  val productId: String,
  val orderId: String,
  val userId: String,
  val quantity: Long,
  val addressId: String,
  val orderStatus: OrderStatus
)
