package com.github.reomor.orderservice.command

import com.github.reomor.core.OrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RejectOrderCommand(
  @TargetAggregateIdentifier
  val orderId: OrderId,
  val reason: String
) {

  private constructor(builder: Builder) : this(
    builder.orderId,
    builder.reason
  )

  class Builder {

    var orderId: OrderId = OrderId.NONE
    var reason: String = ""

    fun build() = RejectOrderCommand(this)
  }

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
  }
}
