package com.github.reomor.orderservice.command.rest.dto

import com.github.reomor.core.OrderId
import com.github.reomor.orderservice.core.OrderStatus

data class CreateOrderResponse(
  val orderId: OrderId,
  val orderStatus: OrderStatus,
  val message: String? = ""
)
