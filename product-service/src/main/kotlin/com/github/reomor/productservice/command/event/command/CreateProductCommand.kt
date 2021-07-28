package com.github.reomor.productservice.command.event.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

typealias ProductId = UUID

data class CreateProductCommand(
  @TargetAggregateIdentifier
  val id: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
) {

  private constructor(builder: Builder) : this(builder.id, builder.name, builder.price, builder.quantity)

  class Builder {

    var id: ProductId = UUID.randomUUID()
    var name: String = ""
    var price: BigDecimal = BigDecimal.ZERO
    var quantity: Int = 0

    fun build() = CreateProductCommand(this)
  }

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
  }
}
