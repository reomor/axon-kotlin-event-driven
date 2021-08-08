package com.github.reomor.orderservice.query

import com.github.reomor.core.OrderId

data class FindOrderQuery(
  val orderId: OrderId
)
