package com.github.reomor.productservice.command.aggregate

import com.github.reomor.core.command.CancelProductReservationCommand
import com.github.reomor.core.command.ReserveProductCommand
import com.github.reomor.core.domain.event.ProductReservationCancelEvent
import com.github.reomor.core.domain.event.ProductReservedEvent
import com.github.reomor.productservice.command.CreateProductCommand
import com.github.reomor.productservice.core.domain.event.ProductCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

@Aggregate
internal class ProductAggregate {

  @AggregateIdentifier
  private lateinit var productId: String
  private lateinit var name: String
  private lateinit var price: BigDecimal
  private var quantity: Long = 0

  @CommandHandler
  constructor(command: CreateProductCommand) {

    if (command.price <= ZERO) throw IllegalArgumentException("The price cannot be less than 0")
    if (command.name.isBlank()) throw IllegalArgumentException("The name cannot be blank")

    AggregateLifecycle.apply(
      ProductCreatedEvent(
        command.productId,
        command.name,
        command.price,
        command.quantity
      )
    )
  }

  @CommandHandler
  fun handle(command: ReserveProductCommand) {

    if (quantity < command.quantity) {
      throw IllegalArgumentException("Insufficient number of products in stock")
    }

    AggregateLifecycle.apply(
      ProductReservedEvent(
        productId = command.productId,
        quantity = command.quantity,
        orderId = command.orderId,
        userId = command.userId
      )
    )
  }

  @CommandHandler
  fun handle(command: CancelProductReservationCommand) {
    AggregateLifecycle.apply(
      ProductReservationCancelEvent(
        productId = command.productId,
        quantity = command.quantity,
        orderId = command.orderId,
        userId = command.userId,
        reason = command.reason
      )
    )
  }

  @EventSourcingHandler
  fun on(event: ProductCreatedEvent) {
    productId = event.productId.asString()
    name = event.name
    price = event.price
    quantity = event.quantity
  }

  @EventSourcingHandler
  fun on(event: ProductReservedEvent) {
    quantity -= event.quantity
  }

  @EventSourcingHandler
  fun on(event: ProductReservationCancelEvent) {
    quantity += event.quantity
  }
}
