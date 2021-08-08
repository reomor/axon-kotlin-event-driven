package com.github.reomor.orderservice.query.handler

import com.github.reomor.orderservice.core.jpa.repository.OrderRepository
import com.github.reomor.orderservice.query.FindOrderQuery
import com.github.reomor.orderservice.query.rest.OrderInfo
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class OrderQueriesHandler(
  private val orderRepository: OrderRepository
) {

  @QueryHandler
  fun findOrder(query: FindOrderQuery): OrderInfo {

    val orderEntity = orderRepository.findByOrderId(query.orderId.asString())

    return if (orderEntity != null) {
      OrderInfo(
        orderId = orderEntity.orderId,
        orderStatus = orderEntity.orderStatus
      )
    } else {
      throw IllegalArgumentException("Order ${query.orderId.asString()} is not found")
    }
  }
}