package com.github.reomor.orderservice.core.event

import com.github.reomor.orderservice.core.domain.event.OrderApprovedEvent
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import com.github.reomor.orderservice.core.domain.event.OrderRejectedEvent
import com.github.reomor.orderservice.core.jpa.entity.OrderEntity
import com.github.reomor.orderservice.core.jpa.repository.OrderRepository
import org.axonframework.eventhandling.EventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderEventsHandler(
  private val orderRepository: OrderRepository
) {

  @EventHandler
  fun on(event: OrderCreatedEvent) {
    orderRepository.save(
      OrderEntity(
        orderId = event.orderId,
        productId = event.productId,
        userId = event.userId,
        addressId = event.addressId,
        quantity = event.quantity,
        orderStatus = event.orderStatus
      )
    )
  }

  @EventHandler
  fun on(event: OrderApprovedEvent) {

    val orderEntity = orderRepository.findByOrderId(event.orderId)

    if (orderEntity == null) {
      return
    }

    orderRepository.save(
      orderEntity.copy(orderStatus = event.orderStatus)
    )
  }

  @EventHandler
  fun on(event: OrderRejectedEvent) {

    val orderEntity = orderRepository.findByOrderId(event.orderId)

    if (orderEntity == null) {
      return
    }

    orderRepository.save(
      orderEntity.copy(orderStatus = event.orderStatus)
    )
  }

  companion object {
    private val log: Logger = LoggerFactory.getLogger(OrderEventsHandler::class.java)
  }
}
