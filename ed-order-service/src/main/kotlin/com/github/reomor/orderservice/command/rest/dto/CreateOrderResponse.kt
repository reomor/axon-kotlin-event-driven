package com.github.reomor.orderservice.command.rest.dto

import com.github.reomor.orderservice.core.OrderId

data class CreateOrderResponse(
  val orderId: OrderId
)
