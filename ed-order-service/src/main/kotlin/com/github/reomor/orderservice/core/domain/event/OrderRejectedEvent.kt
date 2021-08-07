package com.github.reomor.orderservice.core.domain.event

import com.github.reomor.orderservice.core.OrderStatus

data class OrderRejectedEvent(
  val orderId: String,
  val reason: String,
  val orderStatus: OrderStatus = OrderStatus.REJECTED
)
