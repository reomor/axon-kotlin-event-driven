package com.github.reomor.orderservice.command

import com.github.reomor.core.OrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ApproveOrderCommand(
  @TargetAggregateIdentifier
  val orderId: OrderId
) {

  private constructor(builder: Builder) : this(builder.orderId)

  class Builder {
    var orderId: OrderId = OrderId.NONE

    fun build() = ApproveOrderCommand(this)
  }

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
  }
}
