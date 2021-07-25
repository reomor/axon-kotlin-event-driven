package com.github.reomor.productservice.events.command

import com.github.reomor.productservice.events.domain.ProductCreatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.math.BigDecimal.*
import kotlin.IllegalArgumentException

@Aggregate
internal class ProductAggregate {

  @AggregateIdentifier
  private lateinit var id: ProductId
  private lateinit var name: String
  private lateinit var price: BigDecimal
  private var quantity: Int = 0

  @CommandHandler
  constructor(command: CreateProductCommand) {

    if (command.price <= ZERO) throw IllegalArgumentException()
    if (command.name.isBlank()) throw IllegalArgumentException()

    AggregateLifecycle.apply(
      ProductCreatedEvent(
        command.id,
        command.name,
        command.price,
        command.quantity
      )
    )
  }

  @EventSourcingHandler
  fun on(event: ProductCreatedEvent) {
    id = event.id
    name = event.name
    price = event.price
    quantity = event.quantity
  }
}