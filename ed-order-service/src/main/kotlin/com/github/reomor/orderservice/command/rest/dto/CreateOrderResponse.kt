package com.github.reomor.orderservice.command.rest.dto

import com.github.reomor.core.OrderId

data class CreateOrderResponse(
  val orderId: OrderId
)
