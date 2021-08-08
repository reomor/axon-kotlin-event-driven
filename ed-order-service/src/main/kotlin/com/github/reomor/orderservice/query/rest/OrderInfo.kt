package com.github.reomor.orderservice.query.rest

import com.github.reomor.orderservice.core.OrderStatus

class OrderInfo(
  val orderId: String,
  val orderStatus: OrderStatus,
  val message: String? = null
)
