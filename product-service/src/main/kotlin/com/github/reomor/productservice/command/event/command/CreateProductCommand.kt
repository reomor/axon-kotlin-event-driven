package com.github.reomor.productservice.command.event.command

import com.github.reomor.productservice.core.ProductId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

data class CreateProductCommand(
  @TargetAggregateIdentifier
  val productId: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
) {

  private constructor(builder: Builder) : this(
    productId = builder.productId,
    name = builder.name,
    price = builder.price,
    quantity = builder.quantity
  )

  class Builder {

    var productId: ProductId = UUID.randomUUID()
    var name: String = ""
    var price: BigDecimal = BigDecimal.ZERO
    var quantity: Int = 0

    fun build() = CreateProductCommand(this)
  }

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
  }
}
