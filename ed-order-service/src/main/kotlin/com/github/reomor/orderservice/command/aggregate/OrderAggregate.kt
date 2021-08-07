package com.github.reomor.orderservice.command.aggregate

import com.github.reomor.orderservice.command.ApproveOrderCommand
import com.github.reomor.orderservice.command.CreateOrderCommand
import com.github.reomor.orderservice.command.RejectOrderCommand
import com.github.reomor.orderservice.core.*
import com.github.reomor.orderservice.core.domain.event.OrderApprovedEvent
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import com.github.reomor.orderservice.core.domain.event.OrderRejectedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class OrderAggregate {

  @AggregateIdentifier
  private lateinit var orderId: String
  private lateinit var productId: String
  private lateinit var userId: String
  private var quantity: Long = 0
  private lateinit var addressId: String
  private lateinit var orderStatus: OrderStatus

  @CommandHandler
  constructor(command: CreateOrderCommand) {
    AggregateLifecycle.apply(
      OrderCreatedEvent(
        productId = command.productId.asString(),
        orderId = command.orderId.asString(),
        userId = command.userId.asString(),
        quantity = command.quantity,
        addressId = command.addressId.asString(),
        orderStatus = command.orderStatus
      )
    )
  }

  @CommandHandler
  fun handle(command: ApproveOrderCommand) {
    AggregateLifecycle.apply(
      OrderApprovedEvent(
        orderId = command.orderId.asString()
      )
    )
  }

  @CommandHandler
  fun handle(command: RejectOrderCommand) {
    AggregateLifecycle.apply(
      OrderRejectedEvent(
        orderId = command.orderId.asString(),
        reason = command.reason
      )
    )
  }

  @EventSourcingHandler
  fun on(event: OrderCreatedEvent) {
    orderId = event.orderId
    productId = event.productId
    userId = event.userId
    quantity = event.quantity
    addressId = event.addressId
    orderStatus = event.orderStatus
  }

  @EventSourcingHandler
  fun on(event: OrderApprovedEvent) {
    orderStatus = event.orderStatus
  }

  @EventSourcingHandler
  fun on(event: OrderRejectedEvent) {
    orderStatus = event.orderStatus
  }
}
