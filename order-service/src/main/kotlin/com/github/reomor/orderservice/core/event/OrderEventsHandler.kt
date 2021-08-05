package com.github.reomor.orderservice.core.event

import com.github.reomor.orderservice.core.event.domain.OrderCreatedEvent
import com.github.reomor.orderservice.core.jpa.entity.OrderEntity
import com.github.reomor.orderservice.core.jpa.repository.OrderRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class OrderEventsHandler(
  private val orderRepository: OrderRepository
) {

  @EventHandler
  fun on(event: OrderCreatedEvent) {
    orderRepository.save(
      OrderEntity(
        orderId = event.orderId.asString(),
        productId = event.productId.asString(),
        userId = event.userId.asString(),
        addressId = event.addressId.asString(),
        quantity = event.quantity,
        orderStatus = event.orderStatus
      )
    )
  }
}