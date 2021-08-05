package com.github.reomor.orderservice.command.event.command

import com.github.reomor.orderservice.core.*
import com.github.reomor.orderservice.core.event.domain.OrderCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class OrderAggregate {

  @AggregateIdentifier
  private var productId: ProductId = ProductId.NONE
  private var orderId: OrderId = OrderId.NONE
  private var userId: UserId = UserId.NONE
  private var quantity: Long = 0
  private var addressId: AddressId = AddressId.NONE
  private var orderStatus: OrderStatus? = null

  @CommandHandler
  constructor(command: CreateOrderCommand) {
    AggregateLifecycle.apply(
      OrderCreatedEvent(
        productId = command.productId,
        orderId = command.orderId,
        userId = command.userId,
        quantity = command.quantity,
        addressId = command.addressId,
        orderStatus = command.orderStatus
      )
    )
  }

  @EventSourcingHandler
  fun on(event: OrderCreatedEvent) {
    productId = event.productId
    orderId = event.orderId
    userId = event.userId
    quantity = event.quantity
    addressId = event.addressId
    orderStatus = event.orderStatus
  }
}