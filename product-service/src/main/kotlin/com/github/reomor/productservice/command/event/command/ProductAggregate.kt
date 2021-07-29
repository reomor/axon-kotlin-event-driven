package com.github.reomor.productservice.command.event.command

import com.github.reomor.productservice.core.event.domain.ProductCreatedEvent
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

    if (command.price <= ZERO) throw IllegalArgumentException("The price cannot be less than 0")
    if (command.name.isBlank()) throw IllegalArgumentException("The name cannot be blank")

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