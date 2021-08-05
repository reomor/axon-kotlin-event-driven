package com.github.reomor.orderservice.core.event.domain

import com.github.reomor.orderservice.core.*

data class OrderCreatedEvent(
  val productId: ProductId,
  val orderId: OrderId,
  val userId: UserId,
  val quantity: Long,
  val addressId: AddressId,
  val orderStatus: OrderStatus
)
