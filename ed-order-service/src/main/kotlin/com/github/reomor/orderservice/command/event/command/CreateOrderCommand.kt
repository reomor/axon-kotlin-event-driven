package com.github.reomor.orderservice.command.event.command

import com.github.reomor.orderservice.core.*
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateOrderCommand(
  @TargetAggregateIdentifier
  val orderId: OrderId,
  val userId: UserId,
  val productId: ProductId,
  val quantity: Long = 0,
  val addressId: AddressId,
  val orderStatus: OrderStatus
) {

  private constructor(builder: Builder) : this(
    orderId = builder.orderId,
    userId = builder.userId,
    productId = builder.productId,
    quantity = builder.quantity,
    addressId = builder.addressId,
    orderStatus = builder.orderStatus ?: throw IllegalArgumentException("Order status is empty")
  )

  class Builder {

    var orderId: OrderId = OrderId(UUID.randomUUID().toString())
    var userId: UserId = UserId.NONE
    var productId: ProductId = ProductId.NONE
    var quantity: Long = 0
    var addressId: AddressId = AddressId.NONE
    var orderStatus: OrderStatus? = null

    fun build() = CreateOrderCommand(this)
  }

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
  }
}
