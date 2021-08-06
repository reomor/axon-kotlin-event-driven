package com.github.reomor.orderservice.command.rest.dto

import com.github.reomor.orderservice.core.AddressId
import com.github.reomor.orderservice.core.ProductId

data class CreateOrderRequest(
  val productId: ProductId,
  val quantity: Long,
  val addressId: AddressId
)
