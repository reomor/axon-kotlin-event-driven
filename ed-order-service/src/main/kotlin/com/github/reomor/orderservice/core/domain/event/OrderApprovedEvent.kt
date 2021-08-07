package com.github.reomor.orderservice.core.domain.event

import com.github.reomor.orderservice.core.OrderStatus

data class OrderApprovedEvent(
  val orderId: String,
  val orderStatus: OrderStatus = OrderStatus.APPROVED
)
